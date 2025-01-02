package com.trustme.controller;

import com.trustme.dto.TransferDto;
import com.trustme.dto.response.Response;
import com.trustme.dto.response.TransfersResponse;
import com.trustme.enums.StatusCode;
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
    private final TransferService transferService;

    public AdminController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard(){
        return ResponseEntity.status(StatusCode.OK.getHttpStatus()).body("Welcome to admin dashboard");
    }
    @GetMapping("/transfers")
    public ResponseEntity<TransfersResponse> getTransfers(){
        List<TransferDto> transfers = transferService.getAllTransfersHistory();
        TransfersResponse transfersResponse = new TransfersResponse(200,
                StatusCode.OK.getStatusMessage(),
                transfers);
        return ResponseEntity.status(transfersResponse.getCode()).body(transfersResponse);
    }
}
