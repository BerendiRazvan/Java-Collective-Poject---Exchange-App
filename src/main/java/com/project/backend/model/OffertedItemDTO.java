package com.project.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OffertedItemDTO {
    ExchangeItemDTO listedItem;
    List<ExchangeItemDTO> offeredItemList;
}
