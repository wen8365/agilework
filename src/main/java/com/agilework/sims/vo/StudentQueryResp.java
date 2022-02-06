package com.agilework.sims.vo;

import com.agilework.sims.dto.StudentInfo;
import com.agilework.sims.util.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentQueryResp extends BaseResp {
    private StudentInfo student;

    public StudentQueryResp() {
        super();
    }

    public StudentQueryResp(ErrorCode code) {
        super(code);
    }
}
