package com.motorola.demo.service;

import com.motorola.demo.model.BlockedNumberObj;
import com.motorola.demo.model.ContactObj;
import com.motorola.demo.model.PhoneRecordObj;

import java.util.Collection;
import java.util.Optional;

public interface PhoneService {

    Collection<PhoneRecordObj> getAllRecordsByPhoneNumber(String phoneNumber);

    Collection<PhoneRecordObj> getAllRecordsGreaterThanDuration(Integer duration);

    Optional<BlockedNumberObj> getBlockedNumber(String phoneNumber);

    void saveBlockedNumber(BlockedNumberObj blockedNumberObj);

    void removeBlockedNumber(BlockedNumberObj blockedNumberObj);

    Optional<ContactObj> getContact(String phoneNumber);

    void saveContact(ContactObj contactObj);

    void removeContact(ContactObj contactObj);

    public void savePhoneRecord(PhoneRecordObj phoneRecordObj);

    public void savePhoneRecords(Collection<PhoneRecordObj> phoneRecordObjs);

}
