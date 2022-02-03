package com.agilework.sims.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "v_user")
@Data
public class User {
    @Id
    private String userNo;
    private String userName;
    private String password;
    private int role;
}
