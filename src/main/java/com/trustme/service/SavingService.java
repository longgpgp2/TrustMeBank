package com.trustme.service;

import com.trustme.dto.response.SavingResponse;
import com.trustme.enums.SavingStatus;
import com.trustme.enums.TransferStatus;
import com.trustme.mapper.CustomSavingMapper;
import com.trustme.model.Saving;
import com.trustme.repository.SavingRepository;
import com.trustme.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
/**
 * Service class for handling saving-related operations.
 * This class contains methods for saving management.
 */
@Service
public class SavingService {
    private final SavingRepository savingRepository;
    private final AuthService authService;
    public SavingService(SavingRepository savingRepository, AuthService authService) {
        this.savingRepository = savingRepository;
        this.authService = authService;
    }

    /**
     * Create a saving for the requesting user.
     *
     * @param savingName accountName of the intended user
     * @param type is the type of saving
     * @param amount is the intended amount for saving
     * @param duration is the number of months for saving
     * @param interestRate the monthly rate of interest
     * @return new SavingResponse containing SavingDto
     */
    public SavingResponse startSaving(String savingName, String type, Double amount, Double interestRate, int duration){
        Saving saving = Saving.builder()
                .saver(authService.getCurrentUser())
                .name(savingName)
                .type(type)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(duration))
                .createdAt(LocalDateTime.now())
                .status(SavingStatus.ACTIVE)
                .interestRate(interestRate)
                .amount(amount)
                .build();
        return new SavingResponse(201, "New saving created", CustomSavingMapper.getSavingDto(storeSaving(saving)));

    }

    /**
     * store the valid saving to the repository.
     *
     * @param saving the intended saving object
     * @return the stored saving object
     */
    public Saving storeSaving(Saving saving){
        return savingRepository.save(saving);
    }
}
