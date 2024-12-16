package com.trustme.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * @return a response entity indicating the result of the transfer
     */
    public ResponseEntity<String> transferMoney(Double amount, String accountName, String description) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null && !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized!");
        }
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(jwt.getSubject());
        Optional<User> optionalReceiver = userRepository.findByAccountName(accountName);
        if (optionalUser.isEmpty() || optionalReceiver.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid Sender or Receiver!");
        }
        if (!optionalUser.get().validateAccountBalance(amount)) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Insufficient amount!");
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
        return ResponseEntity.status(HttpStatus.OK).body("Transfer Successfully!");

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
