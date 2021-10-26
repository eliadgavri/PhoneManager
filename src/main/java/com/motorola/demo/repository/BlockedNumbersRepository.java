package com.motorola.demo.repository;

import com.motorola.demo.model.BlockedNumberObj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockedNumbersRepository extends JpaRepository<BlockedNumberObj, Integer> {

    Optional<BlockedNumberObj> findByPhoneNumber(String phoneNumber);

}
