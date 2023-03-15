package com.project.backend.exceptions;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
public class ErrorDetails {

  private List<ErrorDetailsItem> errors;

  @Builder
  @Data
  public static class ErrorDetailsItem {

    private String message;
    private String errorCode;
    private Map<String, Object> contextVariables;
  }
}
