package com.motorola.demo.repository;

import com.motorola.demo.model.PhoneRecordObj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PhoneRecordsRepository extends JpaRepository<PhoneRecordObj, Integer> {

    Collection<PhoneRecordObj> findAllByPhoneNumber(String phoneNumber);

    Collection<PhoneRecordObj> findByDurationGreaterThan(Integer phoneNumber);

}
