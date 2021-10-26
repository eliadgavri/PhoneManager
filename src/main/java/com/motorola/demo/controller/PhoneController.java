package com.motorola.demo.controller;

import com.motorola.demo.model.BlockedNumberObj;
import com.motorola.demo.model.ContactObj;
import com.motorola.demo.model.PhoneRecordObj;
import com.motorola.demo.service.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.text.SimpleDateFormat;

@RestController
public class PhoneController {

    @Autowired
    PhoneService phoneService;

    Logger logger = LoggerFactory.getLogger(PhoneController.class);

    /**
     * Responsible for saving new blocked number if it's not already blocked.
     *
     * @param phoneNumber the new blocked phone number
     */

    @GetMapping("/newBlockedNumber")
    public void newBlockedNumber(@RequestParam String phoneNumber) {
        try {
            Optional<BlockedNumberObj> blockedNumber = phoneService.getBlockedNumber(phoneNumber);
            if (blockedNumber.isEmpty()) {
                BlockedNumberObj blockedNumberObj = new BlockedNumberObj();
                blockedNumberObj.setPhoneNumber(phoneNumber);
                phoneService.saveBlockedNumber(blockedNumberObj);
            } else {
                logger.warn("Attempt to add phone number " + phoneNumber + " to blocked list but it is already blocked");
            }
        } catch (Exception ex) {
            logger.error("An error occurred while trying to add phone number " + phoneNumber + " to blocked list", ex);
        }
    }

    /**
     * Responsible for removing blocked number if it's really blocked.
     *
     * @param phoneNumber the blocked phone number
     */

    @GetMapping("/removeBlockedNumber")
    public void removeBlockedNumber(@RequestParam String phoneNumber) {
        try {
            Optional<BlockedNumberObj> blockedNumber = phoneService.getBlockedNumber(phoneNumber);
            if (blockedNumber.isPresent()) {
                phoneService.removeBlockedNumber(blockedNumber.get());
            } else {
                logger.warn("Attempt to remove phone number " + phoneNumber + " from blocked list but it is not a blocked number");
            }
        } catch (Exception ex) {
            logger.error("An error occurred while trying to remove phone number " + phoneNumber + " from blocked list", ex);
        }
    }

    /**
     * Responsible for adding new contact if he's not already exists.
     * If the contact isn't exist the method will update all phone records with this phone number
     * that this number is now a contact.
     *
     * @param name the new contact name
     * @param phoneNumber the new contact phone number
     */

    @GetMapping("/newContact")
    public void newContact(@RequestParam String name, @RequestParam String phoneNumber) {
        try {
            Optional<ContactObj> contact = phoneService.getContact(phoneNumber);
            if (contact.isEmpty()) {
                ContactObj contactObj = new ContactObj();
                contactObj.setName(name);
                contactObj.setPhoneNumber(phoneNumber);
                phoneService.saveContact(contactObj);
                updateContactInPhoneRecords(phoneNumber, true);
            } else {
                logger.warn("Attempt to add phone number " + phoneNumber + " to contact list but it is already there");
            }
        } catch (Exception ex) {
            logger.error("An error occurred while trying to add new phone number " + phoneNumber + " to contacts list", ex);
        }
    }

    /**
     * Responsible for removing contact if he's really exists.
     * If the contact exists the method will update all phone records with this phone number
     * that this number is no longer a contact.
     *
     * @param phoneNumber the contact phone number
     */

    @GetMapping("/removeContact")
    public void removeContact(@RequestParam String phoneNumber) {
        try {
            Optional<ContactObj> contact = phoneService.getContact(phoneNumber);
            if (contact.isPresent()) {
                phoneService.removeContact(contact.get());
                updateContactInPhoneRecords(phoneNumber, false);
            } else {
                logger.warn("Attempt to remove phone number " + phoneNumber + " from contacts but it is not a contact");
            }
        } catch (Exception ex) {
            logger.error("An error occurred while trying to remove phone number " + phoneNumber + " from contacts list", ex);
        }
    }

    /**
     * Responsible for adding new phone record.
     *
     * @throws Exception if the new phone record is from blocked number the method won't save this record.
     * @param time the contact phone number
     * @param callType the call type can be incoming / outgoing
     * @param duration the duration of this phone record
     * @param phoneNumber the record phone number
     */

    @GetMapping("/newPhoneRecord")
    public void newPhoneRecord(@RequestParam String time, @RequestParam String callType, @RequestParam Integer duration, @RequestParam String phoneNumber) throws Exception {
        Optional<BlockedNumberObj> blockedNumber = phoneService.getBlockedNumber(phoneNumber);
        try {
            if (blockedNumber.isPresent()) {
                logger.warn("Attempt to add new phone record from phone number" + phoneNumber + " but this number is in the block list");
                throw new Exception("Phone record from blocked number !!!");
            } else {
                Date date = new SimpleDateFormat("dd-MM-yy hh:mm:ss").parse(time);
                Optional<ContactObj> contactObj = phoneService.getContact(phoneNumber);
                PhoneRecordObj phoneRecordObj = PhoneRecordObj
                        .builder()
                        .time(date)
                        .callType(callType)
                        .duration(duration)
                        .phoneNumber(phoneNumber)
                        .isContact(contactObj.isPresent())
                        .build();
                phoneService.savePhoneRecord(phoneRecordObj);
            }
        } catch (Exception ex) {
            String errorMsg = String.format("An error occurred while trying to add phone record %s %s %d %s to phone records", time, callType, duration, phoneNumber);
            logger.error(errorMsg , ex);
        }
    }

    /**
     * Responsible for getting all phone records that contain the inputted number.
     *
     * @param phoneNumber the record phone number
     * @return collection of phone records
     */

    @GetMapping("/getPhoneRecordsByPhoneNumber")
    public Collection<PhoneRecordObj> getPhoneRecordsByPhoneNumber(@RequestParam String phoneNumber) {
        try {
            return phoneService.getAllRecordsByPhoneNumber(phoneNumber);
        } catch (Exception ex) {
            logger.error("An error occurred while trying to return all phone records made by phone number " + phoneNumber, ex);
            return Collections.emptyList();
        }
    }

    /**
     * Responsible for getting all phone records that has duration greater than the inputted duration.
     *
     * @param duration the record phone duration
     * @return collection of phone records
     */

    @GetMapping("/getAllRecordsGreaterThanDuration")
    public Collection<PhoneRecordObj> getAllRecordsGreaterThanDuration(@RequestParam Integer duration) {
        try {
            return phoneService.getAllRecordsGreaterThanDuration(duration);
        } catch (Exception ex) {
            logger.error("An error occurred while trying to return all phone records with duration greater than " + duration, ex);
            return Collections.emptyList();
        }
    }

    /**
     * Responsible for changing all call records that has the old phone number to the new phone number.
     * If there is a contact that contains the old phone number the method will update his phone number to the new phone number.
     *
     * @param oldPhoneNumber the old phone number
     * @param newPhoneNumber the new phone number
     */

    @GetMapping("/changePhoneNumber")
    public void changePhoneNumber(@RequestParam String oldPhoneNumber, @RequestParam String newPhoneNumber) {
        try {
            ArrayList<PhoneRecordObj> phoneRecordObjs = new ArrayList<>(phoneService.getAllRecordsByPhoneNumber(oldPhoneNumber));
            Optional<ContactObj> contactObj = phoneService.getContact(oldPhoneNumber);
            if (contactObj.isPresent()) {
                contactObj.get().setPhoneNumber(newPhoneNumber);
                phoneService.saveContact(contactObj.get());
            }
            phoneRecordObjs.forEach((phoneRecordObj) -> phoneRecordObj.setPhoneNumber(newPhoneNumber));
            phoneService.savePhoneRecords(phoneRecordObjs);
        } catch (Exception ex) {
            logger.error("An error occurred while trying to change phone number from " + oldPhoneNumber + "to " + newPhoneNumber, ex);
        }
    }

    private void updateContactInPhoneRecords(@RequestParam String phoneNumber, @RequestParam boolean isContact) {
        ArrayList<PhoneRecordObj> phoneRecordObjs = new ArrayList<>(phoneService.getAllRecordsByPhoneNumber(phoneNumber));
        phoneRecordObjs.forEach((phoneRecordObj) -> phoneRecordObj.setContact(isContact));
        phoneService.savePhoneRecords(phoneRecordObjs);
    }

}
