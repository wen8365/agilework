package com.agilework.sims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "v_user")
@Getter
@Setter
public class User {
    @Id
    private String userNo;
    private String userName;
    private String password;
    private int role;
}
