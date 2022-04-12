package com.agilework.sims.dto;

import com.agilework.sims.domain.GradeBase;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GradeInfo extends GradeBase {
    @JsonFormat(timezone = "GMT+8")
    private Date examTime;
    @NonNull
    private String studentNo;
}
