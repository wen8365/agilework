package com.agilework.sims.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_course")
@Data
public class Course {
    @Id
    private String courseNo;
    private String courseCode;
    private String courseName;
    private String major;
    private int published;
    private Date examTime;
    private int examDuration;
    private String examAddr;
}
