
package com.project.backend.model;


import com.project.backend.enums.Scope;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
public class ItemToAddDTO {
    String title;
    String description;
    Long idCustomer;
    Scope scope;
    String image;
}