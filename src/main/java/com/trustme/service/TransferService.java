package com.trustme.service;

import java.time.LocalDateTime;
import java.util.List;

import com.trustme.dto.request.TransferRequest;
import com.trustme.dto.response.TransferResponse;
import com.trustme.enums.StatusCode;
import com.trustme.enums.TransferStatus;
import com.trustme.exception.exceptions.ResourceNotFoundException;
import com.trustme.dto.mapper.CustomTransferMapper;
import com.trustme.model.PendingTransfer;
import com.trustme.repository.PendingTransferRepository;
import org.springframework.stereotype.Service;

import com.trustme.dto.TransferDto;
import com.trustme.model.Transfer;
import com.trustme.model.User;
import com.trustme.repository.TransferRepository;
import com.trustme.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling transfer-related operations.
 * This class contains methods for processing money transfers between users.
 */
@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final PendingTransferRepository pendingTransferRepository;
    private final AuthService authService;
    public TransferService(TransferRepository transferRepository, UserRepository userRepository, PendingTransferRepository pendingTransferRepository, AuthService authService) {
        this.transferRepository = transferRepository;
        this.userRepository = userRepository;
        this.pendingTransferRepository = pendingTransferRepository;
        this.authService = authService;
    }

    /**
     * Processes a money transfer from one user to another.
     *
     * @param amount the amount to transfer
     * @param accountName the account name of the receiver
     * @param description a description of the transfer
     * @return a TransferResponse indicating the result of the transfer
     */
    @Transactional
    public TransferResponse transferMoney(Double amount, String accountName, String description) {
        User sender = authService.getCurrentUser();
        User receiver = userRepository.findByAccountName(accountName)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid receiver account name!"));
        if (!sender.validateAccountBalance(amount) || amount<=0) {
            throw new RuntimeException("Invalid amount!");
        }
        if (sender.getAccountName().equals(receiver.getAccountName())) {
            throw new RuntimeException("Cannot transfer to the same account!");
        }
        Transfer transfer = saveTransfer(sender, receiver, amount, description);
        TransferDto transferDto = CustomTransferMapper.toTransferDto(transfer);
        deletePendingTransfer();
        return new TransferResponse(StatusCode.OK.getHttpStatus(), StatusCode.OK.getStatusMessage(), transferDto);
    }

    /**
     * Save a transfer to the database and return that transfer
     *
     * @param sender a User
     * @param receiver a User
     * @param amount (Double) amount of money
     * @param description String representation of transfer description
     * @return a Transfer object
     * */
    private Transfer saveTransfer(User sender, User receiver, Double amount, String description) {
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        Transfer transfer = Transfer.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .description(description)
                .status(TransferStatus.COMPLETED)
                .build();
        transferRepository.save(transfer);
        userRepository.save(sender);
        userRepository.save(receiver);
        return transfer;
    }

    /**
     * Retrieves the transfer history for a user.
     *
     * @return a list of TransferDto objects representing the user's transfer history
     */
    public List<TransferDto> getAllTransfersHistory() {
        List<Transfer> transfers = transferRepository.findAll();
        return CustomTransferMapper.toTransferDtos(transfers);
    }
    /**
     * Save the transfer request as a PendingTransfer
     *
     * @param transferRequest representing user current working transfer request
     */
    public void startProcessingTransfer(TransferRequest transferRequest){
        pendingTransferRepository.save(
                CustomTransferMapper.toPendingTransfer(
                    transferRequest,
                    authService.getCurrentUser().getAccountName()
                ));
    }
    /**
     * Retrieve the pending transfer
     *
     * @return a pending transfer of the current user
     */
    public PendingTransfer getPendingTransfer(){
        return pendingTransferRepository.findBySenderName(authService.getCurrentUser().getAccountName());
    }
    /**
     * Delete the current user's pending transfer
     */
    public void deletePendingTransfer(){
        pendingTransferRepository.delete(getPendingTransfer());
    }

}
