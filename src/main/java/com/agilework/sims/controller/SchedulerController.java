package com.agilework.sims.controller;

import com.agilework.sims.domain.Session;
import com.agilework.sims.dto.Lesson;
import com.agilework.sims.service.SchedulerService;
import com.agilework.sims.service.SessionService;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.SLogger;
import com.agilework.sims.util.Tuple;
import com.agilework.sims.vo.BaseResp;
import com.agilework.sims.vo.SchedulerQueryResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SchedulerController {
    private static final String TAG = "SchedulerController";

    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private SessionService sessionService;

    @Value("${role.teacher}")
    private int roleTeacher;

    @GetMapping("/queryScheduler")
    public SchedulerQueryResp schedulerQuery(@RequestParam @Nullable String studentNo) {
        SLogger.info(TAG, "query scheduler start, studentNo=" + studentNo);
        SchedulerQueryResp resp = new SchedulerQueryResp();
        Tuple<ErrorCode, List<Lesson>> tuple;
        if (studentNo != null) {
            tuple = schedulerService.querySchedulerByStudentNo(studentNo);
        } else {
            tuple = schedulerService.queryFullScheduler();
        }
        resp.setErrCode(tuple.getFirst().getCode());
        resp.setLessons(tuple.getSecond());
        return resp;
    }

    @PostMapping("/updateScheduler")
    public BaseResp schedulerUpdate(@RequestBody @NonNull List<Lesson> lessons,
                                    @RequestHeader String sessionId) {
        SLogger.info(TAG, "update scheduler start");
        if (lessons.isEmpty()) {
            SLogger.error(TAG, ErrorCode.SCHEDULER_UPDATE_LESSONS_EMPTY.getReason());
            return new BaseResp(ErrorCode.SCHEDULER_UPDATE_LESSONS_EMPTY);
        }
        Session session = sessionService.getSession(sessionId);
        if (session.getUser().getRole() < roleTeacher) {
            SLogger.error(TAG, ErrorCode.SCHEDULER_UPDATE_PERMISSION_DENIED.getReason());
            return new BaseResp(ErrorCode.SCHEDULER_UPDATE_PERMISSION_DENIED);
        }
        ErrorCode errorCode = schedulerService.updateScheduler(lessons);
        return new BaseResp(errorCode);
    }
}
