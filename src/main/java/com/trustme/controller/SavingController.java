package com.trustme.controller;

import com.trustme.dto.request.SavingRequest;
import com.trustme.dto.response.SavingResponse;
import com.trustme.dto.response.SavingsResponse;
import com.trustme.service.SavingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SavingController {
    private final SavingService savingService;

    public SavingController(SavingService savingService) {
        this.savingService = savingService;
    }

    @PostMapping("/savings")
    public ResponseEntity<SavingResponse> postSaving(@RequestBody SavingRequest savingRequest){
        SavingResponse savingResponse = savingService.startSaving(savingRequest.getName(), savingRequest.getType(), savingRequest.getAmount(), 1.0, savingRequest.getDuration());
        return ResponseEntity.ok().body(savingResponse);
    }

    @GetMapping("/savings")
    public ResponseEntity<SavingsResponse> getSavings(){
        SavingsResponse savingsResponse = savingService.retrieveSavings();
        return ResponseEntity.ok().body(savingsResponse);
    }

    @GetMapping("/savings/{id}")
    public ResponseEntity<SavingResponse> getASaving(@PathVariable Long id){
        SavingResponse savingResponse = savingService.retrieveSaving(id);
        return ResponseEntity.ok().body(savingResponse);
    }

    @PostMapping("/savings/cancel/{id}")
    public ResponseEntity<SavingResponse> cancelASaving(@PathVariable Long id){
        SavingResponse savingResponse = savingService.cancelSaving(id);
        return ResponseEntity.ok().body(savingResponse);
    }
}
