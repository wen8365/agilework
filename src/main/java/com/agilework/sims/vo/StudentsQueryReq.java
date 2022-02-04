package com.agilework.sims.vo;

import com.agilework.sims.domain.StudentBase;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@NoArgsConstructor
public class StudentsQueryReq extends StudentBase {
    private String searchText;
    @NonNull
    @JsonProperty("size")
    private int pageSize;
    @NonNull
    @JsonProperty("number")
    private int curPage;

    public String getStudentNo() {
        return studentNo;
    }
}
