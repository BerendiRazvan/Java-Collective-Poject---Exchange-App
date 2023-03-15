package com.project.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemReadyToExchangeDTO {
    ExchangeItemDTO exchangeItemDTO;
    List<UserItemDTO> itemDTOList;
}
