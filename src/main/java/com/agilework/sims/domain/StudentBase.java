package com.agilework.sims.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@MappedSuperclass
public class StudentBase {
    @Getter(AccessLevel.NONE)
    protected String studentNo;  // cannot override id in subclass, so not generate getter for it
    protected String studentName;
    protected String sex;
    protected String major;
    protected Integer grade;
    @JsonProperty("class")
    protected Integer clazz;
}
