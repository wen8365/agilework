package com.agilework.sims.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_course")
@Getter
@Setter
public class Course {
    @Id
    private String courseNo;
    private String courseCode;
    private String courseName;
    private String major;
    private int published;
    @Nullable
    @JsonFormat(timezone = "GMT+8")
    private Date examTime;
    @Nullable
    private int examDuration;
    @Nullable
    private String examAddr;
}
