package com.trustme.controller;

import com.trustme.dto.request.SavingRequest;
import com.trustme.dto.response.SavingResponse;
import com.trustme.service.SavingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SavingController {
    private final SavingService savingService;

    public SavingController(SavingService savingService) {
        this.savingService = savingService;
    }

    @PostMapping("/saving")
    public ResponseEntity<SavingResponse> postSaving(@RequestBody SavingRequest savingRequest){
        SavingResponse savingResponse = savingService.startSaving(savingRequest.getName(), savingRequest.getType(), savingRequest.getAmount(), 1.0, savingRequest.getDuration());
        return ResponseEntity.ok().body(savingResponse);
    }
}
