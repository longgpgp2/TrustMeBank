package com.trustme.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.trustme.dto.response.TransferResponse;
import com.trustme.dto.response.TransfersResponse;
import com.trustme.enums.ErrorCode;
import com.trustme.enums.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.trustme.dto.TransferDto;
import com.trustme.model.Transfer;
import com.trustme.model.User;
import com.trustme.repository.TransferRepository;
import com.trustme.repository.UserRepository;
/**
 * Service class for handling transfer-related operations.
 * This class contains methods for processing money transfers between users.
 */
@Service
// @Transactional
public class TransferService {
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    UserRepository userRepository;
    /**
     * Processes a money transfer from one user to another.
     *
     * @param amount the amount to transfer
     * @param accountName the account name of the receiver
     * @param description a description of the transfer
     * @return a TransferResponse indicating the result of the transfer
     */
    public TransferResponse transferMoney(Double amount, String accountName, String description) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null && !authentication.isAuthenticated()) {
            return new TransferResponse(ErrorCode.UNAUTHORIZED.getHttpStatus(), ErrorCode.UNAUTHORIZED.getErrorMessage(), null);
        }
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(jwt.getSubject());
        Optional<User> optionalReceiver = userRepository.findByAccountName(accountName);
        if (optionalUser.isEmpty() || optionalReceiver.isEmpty()) {
            return new TransferResponse(ErrorCode.RECEIVER_NOT_FOUND.getHttpStatus(), ErrorCode.RECEIVER_NOT_FOUND.getErrorMessage(), null);
        }
        if (!optionalUser.get().validateAccountBalance(amount)) {
            return new TransferResponse(ErrorCode.INVALID_AMOUNT.getHttpStatus(), ErrorCode.INVALID_AMOUNT.getErrorMessage(), null);

        }
        User sender = optionalUser.get();
        User receiver = optionalReceiver.get();
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);
        Transfer transfer = Transfer.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .description(description)
                .build();
        transferRepository.save(transfer);
        userRepository.save(sender);
        userRepository.save(receiver);
        TransferDto transferDto = new TransferDto(
                transfer.getId(),
                transfer.getSender().getAccountName(),
                transfer.getReceiver().getAccountName(),
                transfer.getAmount(),
                transfer.getTimestamp(),
                transfer.getDescription());
        return new TransferResponse(StatusCode.OK.getHttpStatus(), StatusCode.OK.getStatusMessage(), transferDto);

    }
    /**
     * Retrieves the transfer history for a user.
     *
     * @return a list of TransferDto objects representing the user's transfer history
     */
    public List<TransferDto> getAllTransfersHistory() {
        List<Transfer> transfers = transferRepository.findAll();
        return transfers.stream()
                .map(t -> new TransferDto(
                        t.getId(),
                        t.getSender().getAccountName(),
                        t.getReceiver().getAccountName(),
                        t.getAmount(),
                        t.getTimestamp(),
                        t.getDescription()))
                .collect(Collectors.toList());
    }
}
