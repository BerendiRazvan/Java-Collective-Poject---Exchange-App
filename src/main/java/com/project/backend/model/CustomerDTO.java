package com.project.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class CustomerDTO {
    String username;
    String password;
    String email;
}
