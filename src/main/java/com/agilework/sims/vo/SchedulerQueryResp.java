package com.agilework.sims.vo;

import com.agilework.sims.dto.Lesson;
import com.agilework.sims.util.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SchedulerQueryResp extends BaseResp {
    private List<Lesson> lessons;

    public SchedulerQueryResp () {
        super();
    }

    public SchedulerQueryResp(ErrorCode errorCode) {
        super(errorCode);
    }
}
