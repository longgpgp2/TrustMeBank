package com.trustme.mapper;

import com.trustme.dto.TransferDto;
import com.trustme.dto.request.TransferRequest;
import com.trustme.model.PendingTransfer;
import com.trustme.model.Transfer;

import java.util.List;
import java.util.stream.Collectors;

public class CustomTransferMapper {
    public static List<TransferDto> toTransferDtos(List<Transfer> transfers){
        return transfers.stream()
                .map(t -> new TransferDto(
                        t.getId(),
                        t.getSender().getAccountName(),
                        t.getReceiver().getAccountName(),
                        t.getAmount(),
                        t.getTimestamp(),
                        t.getDescription()))
                .collect(Collectors.toList());
    }

    public static TransferDto toTransferDto(Transfer transfer){
        return new TransferDto(
                transfer.getId(),
                transfer.getSender().getAccountName(),
                transfer.getReceiver().getAccountName(),
                transfer.getAmount(),
                transfer.getTimestamp(),
                transfer.getDescription());
    }

    public static PendingTransfer toPendingTransfer(TransferRequest transfer, String name){
        return PendingTransfer.builder()
                .senderName(name)
                .receiverName(transfer.getReceiver())
                .description(transfer.getDescription())
                .amount(transfer.getAmount())
                .build()
                ;
    }
}
