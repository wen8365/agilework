package com.agilework.sims.entity;

import com.agilework.sims.dto.AdminInfo;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "t_admin")
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String teacherNo;
    private String teacherName;
    private String password;
    private String phone;
    private String email;
    private int status = 0;

    public Admin() {}

    public Admin(AdminInfo adminInfo) {
        this.teacherNo = adminInfo.getTeacherNo();
        this.teacherName = adminInfo.getTeacherName();
        this.password = adminInfo.getPassword();
        this.phone = adminInfo.getPhone();
        this.email = adminInfo.getEmail();
    }
}
