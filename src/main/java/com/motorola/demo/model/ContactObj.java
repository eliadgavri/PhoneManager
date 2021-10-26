package com.motorola.demo.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "contacts", schema = "phone_manager")
@Getter @Setter
public class ContactObj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long oid;

    String name;
    String phoneNumber;
}
