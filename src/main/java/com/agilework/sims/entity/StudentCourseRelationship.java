package com.agilework.sims.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_sc_relationship")
@Data
public class StudentCourseRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String courseNo;
    private String studentNo;
    private Date electiveTime;
}
