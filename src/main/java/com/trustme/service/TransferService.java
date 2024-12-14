package com.trustme.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.trustme.model.User;
import com.trustme.repository.TransferRepository;
import com.trustme.repository.UserRepository;

@Service
// @Transactional
public class TransferService {
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<String> transferMoney(Double amount, String accountName) {
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
        userRepository.save(sender);
        userRepository.save(receiver);
        return ResponseEntity.status(HttpStatus.OK).body("Transfer Successfully!");

    }
}
