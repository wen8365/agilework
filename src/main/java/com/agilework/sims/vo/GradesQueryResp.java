package com.agilework.sims.vo;

import com.agilework.sims.dto.GradeSummary;
import com.agilework.sims.util.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GradesQueryResp extends BaseResp {
    List<GradeSummary> gradeSummaries;

    public GradesQueryResp() {
        super();
    }

    public GradesQueryResp(ErrorCode code) {
        super(code);
    }

    public GradesQueryResp(ErrorCode code, List<GradeSummary> gradeSummaries) {
        super(code);
        this.gradeSummaries = gradeSummaries;
    }
}
