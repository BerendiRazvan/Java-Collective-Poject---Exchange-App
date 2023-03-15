package com.project.backend.service;

import com.project.backend.domain.CustomerEty;
import com.project.backend.enums.BusinessErrorCode;
import com.project.backend.exceptions.BusinessException;
import com.project.backend.exceptions.BusinessException.BusinessExceptionElement;
import com.project.backend.model.CustomerDTO;
import com.project.backend.model.UserLoginDTO;
import com.project.backend.repo.CustomerRepo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepo customerRepo;

  @Transactional(readOnly = true)
  public CustomerEty getCustomer(Long id) {
    return customerRepo.findById(id)
        .orElseThrow(() -> new BusinessException(List.of(
            BusinessExceptionElement.builder()
                .errorCode(BusinessErrorCode.CUSTOMER_NOT_FOUND)
                .build())));
  }

  @Transactional
  public void createCustomer(CustomerDTO customerDTO) {
    var customers = customerRepo.findAll()
        .stream()
        .filter(item -> item.getUsername().equals(customerDTO.getUsername()) || item.getEmail()
            .equals(customerDTO.getEmail()))
        .collect(Collectors.toList());
    if (customers.isEmpty()) {
      var newCustomer = new CustomerEty();
      newCustomer.setUsername(customerDTO.getUsername());
      newCustomer.setPassword(customerDTO.getPassword());
      newCustomer.setEmail(customerDTO.getEmail());
      customerRepo.save(newCustomer);
    } else {
      throw new BusinessException(List.of(BusinessException.BusinessExceptionElement.builder()
              .errorCode(BusinessErrorCode.DUPLICATE_DETAILS).build()));
    }
  }

  @Transactional
  public UserLoginDTO login(String email, String password) {
    if(email.isEmpty() || password.isEmpty())
      throw new BusinessException(List.of(
              BusinessExceptionElement.builder()
                      .errorCode(BusinessErrorCode.LOGIN_INVALID_DATA)
                      .build()));

    var customer = customerRepo.findAll().stream()
            .filter(customerEty -> customerEty.getEmail().equals(email))
            .findFirst()
            .orElseThrow(() -> new BusinessException(List.of(
                    BusinessExceptionElement.builder()
                            .errorCode(BusinessErrorCode.LOGIN_EMAIL_INCORRECT)
                            .build())));

    if(password.equals(customer.getPassword()))
      return new UserLoginDTO(customer.getId(), customer.getUsername());
    else
      throw new BusinessException(List.of(
              BusinessExceptionElement.builder()
                      .errorCode(BusinessErrorCode.LOGIN_PASSWORD_INCORRECT)
                      .build()));
  }

}
