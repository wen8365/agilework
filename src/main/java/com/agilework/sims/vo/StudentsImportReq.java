package com.agilework.sims.vo;

import com.agilework.sims.dto.StudentInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
public class StudentsImportReq {
    @NonNull
    public List<StudentInfo> students;
}
