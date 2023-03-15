package com.project.backend.service;

import com.project.backend.domain.CustomerEty;
import com.project.backend.domain.DonationEty;
import com.project.backend.enums.BusinessErrorCode;
import com.project.backend.enums.DonationStatus;
import com.project.backend.enums.ItemStatus;
import com.project.backend.enums.Scope;
import com.project.backend.exceptions.BusinessException;
import com.project.backend.model.DonationDTO;
import com.project.backend.model.UserInfoDTO;
import com.project.backend.repo.CustomerRepo;
import com.project.backend.repo.DonationRepo;
import com.project.backend.repo.ItemRepo;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepo donationRepo;
    private final CustomerRepo customerRepo;
    private final ItemRepo itemRepo;

    public UserInfoDTO createDonation(DonationDTO donationDTO){
        var item= itemRepo.findById(donationDTO.getItemID())
            .orElseThrow(() -> new BusinessException(List.of(
            BusinessException.BusinessExceptionElement.builder()
                .errorCode(BusinessErrorCode.ITEM_NOT_FOUND)
                .build())));

        if(item.getScope()!= Scope.DONATION || item.getStatus() == ItemStatus.CLOSE){
            throw new BusinessException(List.of(
                BusinessException.BusinessExceptionElement.builder()
                    .errorCode(BusinessErrorCode.DONATION_CAN_NOT_BE_DONE)
                    .build()));
        }

        var receivedCustomer=customerRepo.findById(donationDTO.getCustomerID())
            .orElseThrow(() -> new BusinessException(List.of(
                BusinessException.BusinessExceptionElement.builder()
                    .errorCode(BusinessErrorCode.CUSTOMER_NOT_FOUND)
                    .build())));

        item.setStatus(ItemStatus.CLOSE);
        itemRepo.save(item);

        var donation=new DonationEty();
        donation.setItemEty(item);
        donation.setCustomerEty(item.getCustomerEty());
        donation.setReceivedCustomer(receivedCustomer);
        donation.setStatus(DonationStatus.CLOSED);
        donation.setCreationTime(LocalDate.now());
        donationRepo.save(donation);

        return new UserInfoDTO(receivedCustomer.getId(), receivedCustomer.getEmail(), receivedCustomer.getUsername());
    }
}
