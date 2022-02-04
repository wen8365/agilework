package com.agilework.sims.vo;

import com.agilework.sims.dto.StudentInfo;
import com.agilework.sims.util.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentsImportResp extends BaseResp {
    private List<StudentInfo> invalidStudents;
    private String duplicate; // studentNo that has duplicated

    public StudentsImportResp() {
        super();
    }

    public StudentsImportResp(ErrorCode code) {
        super(code);
    }
}
