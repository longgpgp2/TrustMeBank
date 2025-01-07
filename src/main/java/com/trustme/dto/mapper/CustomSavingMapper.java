package com.trustme.dto.mapper;

import com.trustme.dto.SavingDto;
import com.trustme.model.Saving;

public class CustomSavingMapper {
    public static SavingDto getSavingDto(Saving saving){
        return SavingDto.builder()
                .id(saving.getId())
                .name(saving.getName())
                .saverAccountName(saving.getSaver().getAccountName())
                .type(saving.getType())
                .status(saving.getStatus())
                .startDate(saving.getStartDate())
                .endDate(saving.getEndDate())
                .interestRate(saving.getInterestRate())
                .amount(saving.getAmount())
                .createdAt(saving.getCreatedAt())
                .updatedAt(saving.getUpdatedAt())
                .build();
    }
}
