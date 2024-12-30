package com.trustme.repository;

import com.trustme.model.PendingTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingTransferRepository extends JpaRepository<PendingTransfer, Long> {
    PendingTransfer findBySenderName(String senderName);
}
