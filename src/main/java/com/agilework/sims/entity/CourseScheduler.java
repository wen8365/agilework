package com.agilework.sims.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "t_course_schedule")
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
public class CourseScheduler {

    @Getter
    @Setter
    @NoArgsConstructor
    @Embeddable
    @EqualsAndHashCode
    public static class PK implements Serializable {
        private int dayOfWeek;
        private int lessonNo;
        private String courseNo;
    }

    @EmbeddedId
    private PK pk;
    private String address;
    private int startWeek;
    private int endWeek;

    public CourseScheduler() {
        pk = new PK();
    }

    public int getDayOfWeek() {
        return pk.getDayOfWeek();
    }

    public void setDayOfWeek(int dayOfWeek) {
        pk.setDayOfWeek(dayOfWeek);
    }

    public int getLessonNo() {
        return pk.getLessonNo();
    }

    public void setLessonNo(int lessonNo) {
        pk.setLessonNo(lessonNo);
    }

    public String getCourseNo() {
        return pk.getCourseNo();
    }

    public void setCourseNo(String courseNo) {
        pk.setCourseNo(courseNo);
    }
}
