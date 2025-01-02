package com.trustme.repository;

import com.trustme.model.Saving;
import com.trustme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingRepository extends JpaRepository<Saving, Long> {
    List<Saving> findAllBySaver(User user);
    void deleteByIdAndSaver(Long id, User saver);
}
