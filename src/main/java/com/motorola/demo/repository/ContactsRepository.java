package com.motorola.demo.repository;

import com.motorola.demo.model.ContactObj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactsRepository extends JpaRepository<ContactObj, Integer> {

    Optional<ContactObj> findByPhoneNumber(String phoneNumber);

}
