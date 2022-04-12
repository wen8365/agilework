package com.agilework.sims.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "v_grade")
@Getter
@Setter
@ToString
public class GradeV {
    @Getter
    @Setter
    @NoArgsConstructor
    @Embeddable
    @EqualsAndHashCode
    public static class PK implements Serializable {
        private String courseNo;
        private String studentNo;
    }

    @EmbeddedId
    private PK pk;
    private double finalGrade;

    public GradeV() {
        pk = new PK();
    }

    public String getCourseNo() {
        return pk.getCourseNo();
    }

    public void setCourseNo(String courseNo) {
        pk.setCourseNo(courseNo);
    }

    public String getStudentNo() {
        return pk.getStudentNo();
    }

    public void setStudentNo(String studentNo) {
        pk.setStudentNo(studentNo);
    }
}
