package com.agilework.sims.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@ToString
@NoArgsConstructor
@MappedSuperclass
public class GradeBase {
    @JsonProperty("uGrade")
    private double uGrade;
    @JsonProperty("uRatio")
    private int uRatio;
    @JsonProperty("wGrade")
    private double wGrade;
    @JsonProperty("wRatio")
    private int wRatio;
    private double expGrade;
    private int expRatio;
    private double exaGrade;
    private int exaRatio;
}
