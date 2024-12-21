package com.trustme.controller;

import com.trustme.dto.TransferDto;
import com.trustme.dto.response.Response;
import com.trustme.dto.response.TransfersResponse;
import com.trustme.enums.ErrorCode;
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
    @Autowired
    TransferService transferService;
    @GetMapping("/dashboard")
    public Response<String> getDashboard(){
        return new Response(StatusCode.OK.getHttpStatus(),"Welcome to admin dashboard", null);
    }
    @GetMapping("/transfers")
    public TransfersResponse getTransfers(){
        List<TransferDto> transfers = null;
        try{
             transfers = transferService.getAllTransfersHistory();
        } catch (Exception e){
            return new TransfersResponse(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(), ErrorCode.INTERNAL_SERVER_ERROR.getErrorMessage(), null);
        }
        return new TransfersResponse(StatusCode.OK.getHttpStatus(), StatusCode.OK.getStatusMessage(), transfers);
    }
}
