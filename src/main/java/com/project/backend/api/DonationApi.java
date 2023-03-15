package com.project.backend.api;

import com.project.backend.model.DonationDTO;
import com.project.backend.model.UserInfoDTO;
import com.project.backend.service.DonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/donation")
@CrossOrigin
public class DonationApi {
  private final DonationService donationService;

  @PatchMapping
  public ResponseEntity<UserInfoDTO> createDonation(@RequestBody DonationDTO donationDTO) {
    return ResponseEntity.ok(donationService.createDonation(donationDTO));
  }
}
