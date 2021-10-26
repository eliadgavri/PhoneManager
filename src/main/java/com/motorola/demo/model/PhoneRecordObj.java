package com.motorola.demo.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "phone_records", schema = "phone_manager")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PhoneRecordObj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oid;

    Date time;
    String callType;
    Integer duration;
    String phoneNumber;
    boolean isContact;
}
