package com.project.backend.api;

import com.project.backend.domain.ExchangeEty;
import com.project.backend.domain.ItemEty;
import com.project.backend.enums.ExchangeStatus;
import com.project.backend.model.*;
import com.project.backend.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/item")
@CrossOrigin
public class ItemApi {

  private final ItemService itemService;

  @GetMapping("/exchangeItems")
  public ResponseEntity<List<ExchangeItemDTO>> getAllExchangeItem(@RequestParam Long userId) {
    return ResponseEntity.ok(itemService.getAllExchangeItem(userId));
  }


  @GetMapping("/exchangeReadyItems")
  public ResponseEntity<ItemReadyToExchangeDTO> getAllExchangeReadyItems
      (@RequestParam Long itemId, @RequestParam Long userId) {
    return ResponseEntity.ok(itemService.getItemReadyForExchange(itemId, userId));
  }

  @GetMapping
  public ResponseEntity<ItemEty> getItem(@RequestParam Long id) {
    return ResponseEntity.ok(itemService.getItem(id));
  }

  @PostMapping("/addItem")
  public ResponseEntity<ItemEty> addItemForExchange(@RequestBody ItemToAddDTO item) {
    return ResponseEntity.ok(itemService.addItemForExchange(item));
  }

  @GetMapping("/myOffers")
  public ResponseEntity<List<OffertedItemDTO>> getAllMyOffers(@RequestParam Long userId) {
    return ResponseEntity.ok(itemService.getOffertedItems(userId));
  }

  @GetMapping("/myPosts")
  public ResponseEntity<List<ExchangeItemDTO>> getAllMyPosts(@RequestParam Long userId) {
    return ResponseEntity.ok(itemService.getPostsItems(userId));
  }

  @GetMapping("/openDonations")
  public ResponseEntity<List<ItemEty>> getDonationOpenItems(@RequestParam Long userId){
    return ResponseEntity.ok(itemService.getDonationOpenItems(userId));
  }

  @PostMapping("/createExchange")
  public ResponseEntity<ExchangeEty> getAllMyOffers(@RequestParam Long listedItemId,
      @RequestParam Long offeredItemId) {
    return ResponseEntity.ok(itemService.createExchangeRequest(listedItemId, offeredItemId));
  }

  @PatchMapping("/solveExchange")
  public ResponseEntity<Void> acceptDeclineExchange(@RequestParam Long firstItemID, @RequestParam Long secondItemID,
      @RequestParam ExchangeStatus exchangeStatus){
    itemService.acceptDeclineExchange(firstItemID, secondItemID, exchangeStatus);
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @GetMapping("/approvedExchange")
  public ResponseEntity<List<ApprovedExchangesDTO>> getAllApprovedExchanges(@RequestParam Long userId){
      return ResponseEntity.ok(itemService.getAllApprovedExchanges(userId));
  }
}
