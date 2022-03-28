package com.agilework.sims.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@NoArgsConstructor
public class CourseStudentReq {
    @NonNull
    @JsonProperty("size")
    private int pageSize;
    @NonNull
    @JsonProperty("number")
    private int curPage;
    @NonNull
    @JsonProperty("courseNo")
    private String courseNo;
}
