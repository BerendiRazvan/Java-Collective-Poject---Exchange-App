package com.project.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DonationDTO {
    Long itemID;
    Long customerID;
}
