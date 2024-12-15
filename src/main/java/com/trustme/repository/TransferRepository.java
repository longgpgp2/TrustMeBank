package com.trustme.repository;

import com.trustme.model.Transfer;
import com.trustme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<User> findBySender(User sender);
    List<User> findByReceiver(User receiver);

}
