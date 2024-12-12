package com.trustme.controller;

import com.trustme.dto.request.TransferRequest;
import com.trustme.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransferController {
    @Autowired
    TransferService transferService;
    @GetMapping("/transfer")
    public ResponseEntity<String> getTransfer(){
        return ResponseEntity.ok("Choose an account to transfer money to.");
    }
    @PostMapping("/transfer")
    public ResponseEntity<String> postTransfer(@RequestBody TransferRequest transferRequest){
        return transferService.transferMoney(transferRequest.getAmount(),transferRequest.getReceiver());
    }
}
