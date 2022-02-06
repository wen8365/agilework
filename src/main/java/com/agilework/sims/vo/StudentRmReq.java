package com.agilework.sims.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StudentRmReq {
    @NonNull
    private List<String> studentNoList;
}
