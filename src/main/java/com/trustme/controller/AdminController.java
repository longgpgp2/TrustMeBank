package com.trustme.controller;

import com.trustme.dto.TransferDto;
import com.trustme.model.Transfer;
import com.trustme.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    TransferService transferService;
    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard(){
        return ResponseEntity.ok().body("Welcome to admin dashboard");
    }
    @GetMapping("/transfers")
    public ResponseEntity<List<TransferDto>> getTransfers(){
        List<TransferDto> transfers = null;
        try{
             transfers = transferService.getAllTransfersHistory();
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(null);
        }
        return ResponseEntity.ok().body(transfers);
    }
}
