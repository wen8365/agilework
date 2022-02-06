package com.agilework.sims.vo;

import com.agilework.sims.dto.StudentInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
public class StudentUpdateReq {
    @JsonProperty("student")
    @NonNull
    private StudentInfo studentInfo;
}
