package com.agilework.sims.entity;

import com.agilework.sims.domain.GradeBase;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_course_grade")
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
public class Grade extends GradeBase {

    @Getter
    @Setter
    @NoArgsConstructor
    @Embeddable
    @EqualsAndHashCode
    public static class PK implements Serializable {
        private Date examTime;
        private String courseNo;
        private String studentNo;
    }

    @EmbeddedId
    private PK pk;

    public Grade() {
        pk = new PK();
    }

    public Date getExamTime() {
        return pk.getExamTime();
    }

    public void setExamTime(Date examTime) {
        pk.setExamTime(examTime);
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
