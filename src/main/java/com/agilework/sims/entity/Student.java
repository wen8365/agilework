package com.agilework.sims.entity;

import com.agilework.sims.domain.StudentBase;
import com.agilework.sims.dto.StudentInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "t_student")
@Getter
@Setter
@AttributeOverride(name = "clazz", column = @Column(name = "class"))
public class Student extends StudentBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String password;
    private int status;

    public Student() {
        super();
    }

    public String getStudentNo() {
        return studentNo;
    }
}
