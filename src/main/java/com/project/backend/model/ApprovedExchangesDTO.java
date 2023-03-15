package com.project.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class ApprovedExchangesDTO {
    String firstItemTitle;
    String secondItemTitle;
    String usernameFirstCustomer;
    String emailFirstCustomer;
    String usernameSecondCustomer;
    String emailSecondCustomer;
    LocalDate creationTime;
}