package com.agilework.sims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Lesson {
    private int dayOfWeek;
    private int lessonNo;
    private String courseNo;
    private String courseName;
    private String address;
    private int startWeek;
    private int endWeek;
}
