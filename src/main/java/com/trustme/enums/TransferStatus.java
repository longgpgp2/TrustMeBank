package com.trustme.enums;

public enum TransferStatus {
    PENDING( "Transaction is been processing!"),
    COMPLETED( "Transaction is completed!"),
    FAILED( "Transaction is failed!");

    private String statusMessage;

    TransferStatus(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
