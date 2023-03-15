package com.project.backend.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class ExchangeItemDTO {
    Long id;
    String title;
    String email;
    LocalDate creationTime;
    String customerName;
    String description;
    String image;
}
