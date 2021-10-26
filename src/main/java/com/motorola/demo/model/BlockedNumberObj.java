package com.motorola.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "blocked_numbers", schema = "phone_manager")
@Getter @Setter
public class BlockedNumberObj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long oid;

    String phoneNumber;
}
