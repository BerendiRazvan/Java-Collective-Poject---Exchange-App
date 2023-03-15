package com.project.backend.api;

import com.project.backend.domain.CustomerEty;
import com.project.backend.model.CustomerDTO;
import com.project.backend.model.UserLoginDTO;
import com.project.backend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerApi {
  private final CustomerService customerService;

  @GetMapping
  public ResponseEntity<CustomerEty> getCustomer(@RequestParam Long id) {
    return ResponseEntity.ok(customerService.getCustomer(id));
  }

  @PostMapping
  public ResponseEntity<Void> addCustomer(@RequestBody CustomerDTO customerDTO) {
    customerService.createCustomer(customerDTO);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PatchMapping("/login")
  public ResponseEntity<UserLoginDTO> login(@RequestParam String email, @RequestParam String password) {
    return ResponseEntity.ok(customerService.login(email, password));
  }

}
