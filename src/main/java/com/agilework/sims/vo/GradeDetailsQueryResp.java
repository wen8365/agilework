package com.agilework.sims.vo;

import com.agilework.sims.dto.GradeInfo;
import com.agilework.sims.util.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GradeDetailsQueryResp extends BaseResp {
    private String courseNo;
    private List<GradeInfo> gradeDetailList;

    public GradeDetailsQueryResp() {
        super();
    }

    public GradeDetailsQueryResp(ErrorCode code) {
        super(code);
    }
}
