package com.project.backend.service;

import com.project.backend.domain.ExchangeEty;
import com.project.backend.domain.ItemEty;
import com.project.backend.enums.*;
import com.project.backend.exceptions.BusinessException;
import com.project.backend.exceptions.BusinessException.BusinessExceptionElement;
import com.project.backend.model.*;
import com.project.backend.repo.ExchangeRepo;
import com.project.backend.repo.ItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepo itemRepo;
  private final CustomerService customerService;
  private final ExchangeRepo exchangeRepo;

  public ExchangeEty createExchangeRequest(Long listedItemId, Long offeredItemId) {
    var listedItem = itemRepo.findById(listedItemId)
        .orElseThrow(() -> new BusinessException(List.of(
            BusinessExceptionElement.builder()
                .errorCode(BusinessErrorCode.ITEM_NOT_FOUND)
                .build())));
    var offeredItem = itemRepo.findById(offeredItemId)
        .orElseThrow(() -> new BusinessException(List.of(
            BusinessExceptionElement.builder()
                .errorCode(BusinessErrorCode.ITEM_NOT_FOUND)
                .build())));
    var exchangeEty = new ExchangeEty();
    exchangeEty.setListedItemEty(listedItem);
    exchangeEty.setOfferedItemEty(offeredItem);
    exchangeEty.setStatus(ExchangeStatus.PENDING);
    exchangeEty.setCreationTime(LocalDate.now());
    return exchangeRepo.save(exchangeEty);
  }

  public void acceptDeclineExchange(Long firstTimeID, Long secondItemID, ExchangeStatus exchangeStatus) {
    var listedItemEty = itemRepo.findById(firstTimeID)
        .orElseThrow(() -> new BusinessException(List.of(
            BusinessExceptionElement.builder()
                .errorCode(BusinessErrorCode.ITEM_NOT_FOUND)
                .build())));

    var offeredItemEty = itemRepo.findById(secondItemID)
        .orElseThrow(() -> new BusinessException(List.of(
            BusinessExceptionElement.builder()
                .errorCode(BusinessErrorCode.ITEM_NOT_FOUND)
                .build())));

    var customers = exchangeRepo.findAll().stream()
        .filter(
            item -> (item.getListedItemEty().getId().equals(firstTimeID) && item.getOfferedItemEty()
                .getId().equals(secondItemID))
                || (item.getListedItemEty().getId().equals(secondItemID) && item.getOfferedItemEty()
                .getId().equals(firstTimeID))).toList();
    var exchangeEty = customers.get(0);

    if (exchangeEty == null) {
      throw new BusinessException(List.of(BusinessException.BusinessExceptionElement.builder()
          .errorCode(BusinessErrorCode.EXCHANGE_NOT_FOUND).build()));
    }

    if (exchangeEty.getStatus() != ExchangeStatus.PENDING || listedItemEty.getStatus().equals(ItemStatus.CLOSE) || offeredItemEty.equals(ItemStatus.CLOSE)) {
      throw new BusinessException(List.of(BusinessException.BusinessExceptionElement.builder()
          .errorCode(BusinessErrorCode.EXCHANGE_CAN_NOT_BE_DONE).build()));
    }

    if (exchangeStatus.equals(ExchangeStatus.APPROVED)) {
      listedItemEty.setStatus(ItemStatus.CLOSE);
      offeredItemEty.setStatus(ItemStatus.CLOSE);
      itemRepo.save(listedItemEty);
      itemRepo.save(offeredItemEty);
      var exchanges = exchangeRepo.findAll().stream()
              .filter(
                      item -> item.getListedItemEty().getId().equals(firstTimeID) || item.getOfferedItemEty().getId().equals(firstTimeID) ||
                              item.getListedItemEty().getId().equals(secondItemID) || item.getOfferedItemEty().getId().equals(secondItemID)).toList();
      exchangeRepo.deleteAll(exchanges);
    }

    exchangeEty.setStatus(exchangeStatus);
    exchangeRepo.save(exchangeEty);
  }

  @Transactional
  public List<ApprovedExchangesDTO> getAllApprovedExchanges(Long userId) {
    return exchangeRepo.findAll().stream()
        .filter(exchangeEty -> exchangeEty.getStatus() == ExchangeStatus.APPROVED &&
            (exchangeEty.getOfferedItemEty().getCustomerEty().getId()==userId || exchangeEty.getListedItemEty().getCustomerEty().getId()==userId))
        .map(item -> new ApprovedExchangesDTO(
            item.getListedItemEty().getTitle(),
            item.getOfferedItemEty().getTitle(),
            item.getListedItemEty().getCustomerEty().getUsername(),
            item.getListedItemEty().getCustomerEty().getEmail(),
            item.getOfferedItemEty().getCustomerEty().getUsername(),
            item.getOfferedItemEty().getCustomerEty().getEmail(),
            item.getCreationTime()))
        .toList();
  }

  @Transactional
  public ItemReadyToExchangeDTO getItemReadyForExchange(Long itemId, Long userId) {
    var itemObject = itemRepo.findById(itemId)
        .orElseThrow(() -> new BusinessException(List.of(
            BusinessExceptionElement.builder()
                .errorCode(BusinessErrorCode.ITEM_NOT_FOUND)
                .build())));

    var title = itemObject.getTitle();
    var email = itemObject.getCustomerEty().getEmail();
    var description = itemObject.getDescription();
    var image = itemObject.getImage();
    var creationTime = itemObject.getCreationTime();
    var userItemDTOList = itemRepo.findAll()
        .stream()
        .filter(item -> item.getCustomerEty().getId().equals(userId) && !item.equals(itemObject) && item.getStatus() != ItemStatus.CLOSE)
        .map(item -> new UserItemDTO(
            item.getId(),
            item.getTitle(),
            item.getCreationTime(),
            item.getDescription(),
            item.getCustomerEty().getUsername(),
            item.getImage()))
        .sorted((item1, item2) -> item1.getCreationTime().compareTo(item2.getCreationTime()))
        .toList();
    var exchangeItemDTO = new ExchangeItemDTO(
        itemObject.getId(),
        title,
        email,
        creationTime,
        description,
        itemObject.getCustomerEty().getUsername(),
        image);
    return new ItemReadyToExchangeDTO(exchangeItemDTO, userItemDTOList);
  }

  @Transactional
  public List<ExchangeItemDTO> getAllExchangeItem(Long userId) {
    return itemRepo.findAll()
        .stream()
        .filter(itemEty -> !itemEty.getCustomerEty().getId().equals(userId) && itemEty.getScope() == Scope.EXCHANGE && (
            itemEty.getStatus() == ItemStatus.PENDING || itemEty.getStatus() == ItemStatus.OPEN))
        .map(exchangeItem ->
            new ExchangeItemDTO(
                exchangeItem.getId(),
                exchangeItem.getTitle(),
                exchangeItem.getCustomerEty().getEmail(),
                exchangeItem.getCreationTime(),
                exchangeItem.getCustomerEty().getUsername(),
                exchangeItem.getDescription(),
                exchangeItem.getImage()
            ))
        .collect(Collectors.toList());
  }

  @Transactional
  public ItemEty getItem(Long id) {
    return itemRepo.findById(id)
        .orElseThrow(() -> new BusinessException(List.of(
            BusinessException.BusinessExceptionElement.builder()
                .errorCode(BusinessErrorCode.ITEM_NOT_FOUND)
                .build())));
  }

  @Transactional
  public ItemEty addItemForExchange(ItemToAddDTO item) {
    var newItem = new ItemEty();
    newItem.setTitle(item.getTitle());
    newItem.setDescription(item.getDescription());
    newItem.setStatus(ItemStatus.OPEN);
    newItem.setCreationTime(LocalDate.now());
    newItem.setCustomerEty(customerService.getCustomer(item.getIdCustomer()));
    newItem.setScope(item.getScope());
    newItem.setImage(item.getImage());
    return itemRepo.save(newItem);
  }

  public List<ExchangeItemDTO> getPostsItems(Long userId) {
    var myItems = itemRepo.findAll()
        .stream()
        .filter(item -> item.getCustomerEty().getId() == userId && item.getStatus()!=ItemStatus.CLOSE).toList();
    return myItems.stream()
        .map(myItem ->
            new ExchangeItemDTO(
                myItem.getId(),
                myItem.getTitle(),
                myItem.getCustomerEty().getEmail(),
                myItem.getCreationTime(),
                myItem.getCustomerEty().getUsername(),
                myItem.getDescription(),
                myItem.getImage()))
        .collect(Collectors.toList());
  }

  public List<OffertedItemDTO> getOffertedItems(Long userId) {
    var myItems = itemRepo.findAll()
        .stream()
        .filter(item -> item.getCustomerEty().getId().equals(userId)
            && item.getStatus()!=ItemStatus.CLOSE).toList();

    return myItems.stream()
        .map(myItem ->
            new OffertedItemDTO(
                new ExchangeItemDTO(
                    myItem.getId(),
                    myItem.getTitle(),
                    myItem.getCustomerEty().getEmail(),
                    myItem.getCreationTime(),
                    myItem.getCustomerEty().getUsername(),
                    myItem.getDescription(),
                    myItem.getImage()),
                exchangeRepo.findAll()
                    .stream()
                    .filter(exchangeEty -> exchangeEty.getListedItemEty().getId().equals(myItem.getId())
                        && exchangeEty.getStatus()==ExchangeStatus.PENDING)
                    .map(exchangeEty -> new ExchangeItemDTO(
                        exchangeEty.getOfferedItemEty().getId(),
                        exchangeEty.getOfferedItemEty().getTitle(),
                        exchangeEty.getOfferedItemEty().getCustomerEty().getEmail(),
                        exchangeEty.getOfferedItemEty().getCreationTime(),
                        exchangeEty.getOfferedItemEty().getCustomerEty().getUsername(),
                        exchangeEty.getOfferedItemEty().getDescription(),
                        exchangeEty.getOfferedItemEty().getImage()))
                    .collect(Collectors.toList())
            ))
        .collect(Collectors.toList());
  }

  public List<ItemEty> getDonationOpenItems(Long userId){
    return itemRepo.findAll().stream()
            .filter(x -> x.getStatus().equals(ItemStatus.OPEN) && x.getScope().equals(Scope.DONATION) && (!x.getCustomerEty().getId().equals(userId)))
            .toList();
  }

}
