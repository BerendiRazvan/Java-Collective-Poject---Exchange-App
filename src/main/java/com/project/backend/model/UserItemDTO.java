package com.project.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserItemDTO {
    Long Id;
    String title;
    LocalDate creationTime;
    String description;
    String username;
    String image;
}
