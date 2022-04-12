package com.agilework.sims.vo;

import com.agilework.sims.dto.GradeInfo;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GradesImportReq {
    private String courseNo;
    @NonNull
    private List<GradeInfo> grades;
}
