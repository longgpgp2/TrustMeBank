package com.trustme.controller;

import com.trustme.dto.request.LoanRequest;
import com.trustme.dto.response.LoanResponse;
import com.trustme.dto.response.LoansResponse;
import com.trustme.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;
    @GetMapping()
    public ResponseEntity<LoansResponse> getUserLoans(){
        return ResponseEntity.ok().body(loanService.getAllLoansOfCurrentUser());
    }
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getUserLoan(@PathVariable Long id){
        return ResponseEntity.ok().body(loanService.getOneLoanOfCurrentUser(id));
    }
    @PostMapping()
    public ResponseEntity<LoanResponse> postUserLoan(@RequestBody LoanRequest loanRequest){
        return ResponseEntity.ok().body(loanService.requestLoan(loanRequest));
    }
}
