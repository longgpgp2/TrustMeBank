package com.trustme.controller;

import com.trustme.dto.TransferDto;
import com.trustme.dto.request.LoanEditRequest;
import com.trustme.dto.request.LoanRequest;
import com.trustme.dto.response.LoanResponse;
import com.trustme.dto.response.LoansResponse;
import com.trustme.dto.response.Response;
import com.trustme.dto.response.TransfersResponse;
import com.trustme.enums.StatusCode;
import com.trustme.model.Transfer;
import com.trustme.service.LoanService;
import com.trustme.service.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController()
@RequestMapping("/admin")
public class AdminController {
    private final TransferService transferService;
    private final LoanService loanService;

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
    @GetMapping("/loans")
    public ResponseEntity<LoansResponse> getLoans(){
        return ResponseEntity.ok().body(loanService.getAllLoans());
    }
    @GetMapping("/loans/{id}")
    public ResponseEntity<LoanResponse> getLoan(@PathVariable Long id){
        return ResponseEntity.ok().body(loanService.getOneLoan(id));
    }
    @PatchMapping("/loans/{id}")
    public ResponseEntity<LoanResponse> editLoan(@RequestBody LoanEditRequest loanEditRequest, @PathVariable Long id){
        return ResponseEntity.ok().body(loanService.editLoan(loanEditRequest, id));
    }

}
