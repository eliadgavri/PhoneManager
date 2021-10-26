package com.motorola.demo.service;

import com.motorola.demo.model.BlockedNumberObj;
import com.motorola.demo.model.ContactObj;
import com.motorola.demo.model.PhoneRecordObj;
import com.motorola.demo.repository.BlockedNumbersRepository;
import com.motorola.demo.repository.ContactsRepository;
import com.motorola.demo.repository.PhoneRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PhoneServiceImpl implements PhoneService {

    @Autowired
    private BlockedNumbersRepository blockedNumbersRepository;

    @Autowired
    private PhoneRecordsRepository phoneRecordsRepository;

    @Autowired
    private ContactsRepository contactsRepository;

    @Override
    public Collection<PhoneRecordObj> getAllRecordsByPhoneNumber(String phoneNumber) {
        return phoneRecordsRepository.findAllByPhoneNumber(phoneNumber);
    }

    @Override
    public Collection<PhoneRecordObj> getAllRecordsGreaterThanDuration(Integer duration) {
        return phoneRecordsRepository.findByDurationGreaterThan(duration);
    }

    @Override
    public Optional<BlockedNumberObj> getBlockedNumber(String phoneNumber) {
        return blockedNumbersRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public void saveBlockedNumber(BlockedNumberObj blockedNumberObj) {
        blockedNumbersRepository.save(blockedNumberObj);
    }

    @Override
    public void removeBlockedNumber(BlockedNumberObj blockedNumberObj) {
        blockedNumbersRepository.delete(blockedNumberObj);
    }

    @Override
    public Optional<ContactObj> getContact(String phoneNumber) {
        return contactsRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public void saveContact(ContactObj contactObj) {
        contactsRepository.save(contactObj);
    }

    @Override
    public void removeContact(ContactObj contactObj) {
        contactsRepository.delete(contactObj);
    }

    @Override
    public void savePhoneRecord(PhoneRecordObj phoneRecordObj) {
        phoneRecordsRepository.save(phoneRecordObj);
    }

    @Override
    public void savePhoneRecords(Collection<PhoneRecordObj> phoneRecordObjs) {
        phoneRecordsRepository.saveAll(phoneRecordObjs);
    }
}
