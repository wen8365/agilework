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

    public Student(StudentInfo info) {
        copy(info);
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void copy(StudentInfo info) {
        this.studentNo = info.getStudentNo();
        this.studentName = info.getStudentName();
        this.sex = info.getSex();
        this.major = info.getMajor();
        this.grade = info.getGrade();
        this.clazz = info.getClazz();
        this.password = info.getPassword();
    }
}
