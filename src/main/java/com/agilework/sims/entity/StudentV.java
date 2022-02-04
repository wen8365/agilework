package com.agilework.sims.entity;

import com.agilework.sims.domain.StudentBase;

import javax.persistence.*;

/**
 * Student entity for view
 */
@Entity
@Table(name = "v_student")
@AttributeOverride(name = "clazz", column = @Column(name = "class"))
public class StudentV extends StudentBase {

    public StudentV() {
        super();
    }

    @Id
    public String getStudentNo() {
        return studentNo;
    }
}
