package com.trustme.service;

import com.trustme.dto.SavingDto;
import com.trustme.dto.response.SavingResponse;
import com.trustme.dto.response.SavingsResponse;
import com.trustme.enums.SavingStatus;
import com.trustme.exception.exceptions.ResourceNotFoundException;
import com.trustme.dto.mapper.CustomSavingMapper;
import com.trustme.model.Saving;
import com.trustme.model.User;
import com.trustme.repository.SavingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
     * retrieve all the savings of the current user.
     *
     * @return the stored saving objects
     */
    public SavingsResponse retrieveSavings() {
        List<Saving> savings = savingRepository.findAllBySaver(authService.getCurrentUser());
        List<SavingDto> savingDtos = savings.stream().map(CustomSavingMapper::getSavingDto).toList();
        return new SavingsResponse(200, "Retrieved successfully", savingDtos);
    }
    /**
     * Retrieve a specific saving of the current user.
     *
     * @param id the id of the intended saving object
     * @return the stored saving object
     * @throws ResourceNotFoundException if the saving object is not found
     * @throws IllegalArgumentException if the provided id is null
     */
    public SavingResponse retrieveSaving(Long id) throws ResourceNotFoundException, IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("The saving ID cannot be null");
        }
        Saving saving = savingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Saving not found with id: " + id));
        SavingDto savingDto = CustomSavingMapper.getSavingDto(saving);

        return new SavingResponse(200, "Retrieved successfully", savingDto);
    }

    /**
     * Remove a specific saving of the current user.
     *
     * @param id the id of the saving to remove
     * @return no content
     */
    public SavingResponse cancelSaving(Long id) {
        User user = authService.getCurrentUser();
        Saving saving = savingRepository.findById(id).get();
        user.setBalance(user.getBalance()+saving.getAmount());
        authService.saveUser(user);
        removeSaving(saving.getId());
        return new SavingResponse(204, "Deleted successfully", null);
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

    /**
     * remove a saving of the current user from the repository.
     *
     * @param savingId the id of the intended saving to remove
     */
    public void removeSaving(Long savingId) {
        savingRepository.deleteByIdAndSaver(savingId, authService.getCurrentUser());}
}
