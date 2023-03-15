package com.project.backend.api;


import com.project.backend.exceptions.BusinessException;
import com.project.backend.exceptions.ErrorDetails;
import com.project.backend.exceptions.ErrorDetails.ErrorDetailsItem;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorDetails> handleBusinessException(BusinessException exception) {

    Optional<HttpStatus> serverStatusError = exception.getErrors().stream()
        .filter(errorItem -> errorItem.getErrorCode().getStatus().is5xxServerError())
        .map(errorItem -> errorItem.getErrorCode().getStatus()).findAny();

    var status = serverStatusError.orElseGet(() -> exception.getErrors().stream()
        .filter(errorItem -> errorItem.getErrorCode().getStatus().is4xxClientError())
        .map(errorItem -> errorItem.getErrorCode().getStatus())
        .findAny()
        .orElse(HttpStatus.BAD_REQUEST));
    var errorDetails = new ErrorDetails();
    errorDetails.setErrors(exception.getErrors().stream()
        .map(e -> ErrorDetailsItem.builder()
            .errorCode(e.getErrorCode().name())
            .build()).collect(Collectors.toList()));

    return ResponseEntity.status(status)
        .body(errorDetails);
  }

  @ExceptionHandler(value= MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDetails> handleException(MethodArgumentNotValidException exception) {

    var errorDetails = new ErrorDetails();
    errorDetails.setErrors(List.of( ErrorDetailsItem.builder()
            .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
            .message(exception.getBindingResult().getAllErrors().stream()
                    .map(objectError -> Objects.requireNonNull(objectError.getCodes())[0]).collect(Collectors.joining(",")))
            .build()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
  }

  @ExceptionHandler(value= HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDetails> handleException(HttpMessageNotReadableException exception) {

    var errorDetails = new ErrorDetails();
    errorDetails.setErrors(List.of(ErrorDetailsItem.builder()
            .errorCode("message_not_readable")
            .build()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
  }


}
