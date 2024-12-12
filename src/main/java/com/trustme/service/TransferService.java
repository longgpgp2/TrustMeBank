package com.trustme.service;

import com.trustme.model.CustomUserDetails;
import com.trustme.model.User;
import com.trustme.repository.TransferRepository;
import com.trustme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
//@Transactional
public class TransferService {
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    UserRepository userRepository;
    public ResponseEntity<String> transferMoney(Double amount, String accountName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null && !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized!");
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Optional<User> optionalReceiver = userRepository.findByAccountName(accountName);
        if(!user.validateAccountBalance(amount) || optionalReceiver.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Invalid Sender or Amount!");
        }
        User receiver = optionalReceiver.get();
        user.setBalance(user.getBalance()-amount);
        receiver.setBalance(receiver.getBalance()+amount);
        userRepository.save(user);
        userRepository.save(receiver);
        return ResponseEntity.status(HttpStatus.OK).body("Transfer Successfully!");

    }
}
