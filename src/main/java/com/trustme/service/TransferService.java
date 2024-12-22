package com.trustme.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.trustme.constant.ConstantResponses;
import com.trustme.dto.response.TransferResponse;
import com.trustme.dto.response.TransfersResponse;
import com.trustme.enums.ErrorCode;
import com.trustme.enums.StatusCode;
import com.trustme.mapper.CustomTransferMapper;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling transfer-related operations.
 * This class contains methods for processing money transfers between users.
 */
@Service
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
    @Transactional
    public TransferResponse transferMoney(Double amount, String accountName, String description) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null && !authentication.isAuthenticated()) {
            return new TransferResponse(ErrorCode.UNAUTHORIZED.getHttpStatus(), ErrorCode.UNAUTHORIZED.getErrorMessage(), null);
        }
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(jwt.getSubject());
        Optional<User> optionalReceiver = userRepository.findByAccountName(accountName);
        if (optionalUser.isEmpty() || optionalReceiver.isEmpty()) {
            return ConstantResponses.INVALID_RECEIVER;
        }
        if (!optionalUser.get().validateAccountBalance(amount) || amount<=0) {
            return ConstantResponses.INVALID_AMOUNT;
        }
        User sender = optionalUser.get();
        User receiver = optionalReceiver.get();
        if (sender.getAccountName().equals(receiver.getAccountName())) {
            return ConstantResponses.INVALID_RECEIVER;
        }
        Transfer transfer = saveTransfer(sender, receiver, amount, description);
        TransferDto transferDto = CustomTransferMapper.toTransferDto(transfer);
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
}
