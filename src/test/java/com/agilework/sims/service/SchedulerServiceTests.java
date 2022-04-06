package com.agilework.sims.service;

import com.agilework.sims.dto.Lesson;
import com.agilework.sims.repository.CourseRepository;
import com.agilework.sims.repository.SchedulerRepository;
import com.agilework.sims.repository.StuCourseRelationshipRepository;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerServiceTests {

    @InjectMocks
    private SchedulerService schedulerService;
    @Mock
    private StuCourseRelationshipRepository scRepository;
    @Mock
    private SchedulerRepository schedulerRepository;
    @Mock
    private CourseRepository courseRepository;

    @Before
    public void init() {
        ReflectionTestUtils.setField(schedulerService, "weekStart", 1);
        ReflectionTestUtils.setField(schedulerService, "weekEnd", 18);
        ReflectionTestUtils.setField(schedulerService, "lessonStart", 1);
        ReflectionTestUtils.setField(schedulerService, "lessonEnd", 11);
    }

    @Test
    public void testQuerySchedulerByStudentNo() {
        List<String> courseNos = new ArrayList<>();
        courseNos.add("1001");
        courseNos.add("1002");

        Mockito.when(scRepository.findByStudentNo(Mockito.anyString())).thenReturn(courseNos);
        Mockito.when(schedulerRepository.findByCourseNoIn(Mockito.anyList())).thenReturn(new ArrayList<>());
        Mockito.when(courseRepository.findByCourseNoIn(Mockito.anyList())).thenReturn(new ArrayList<>());

        Tuple<ErrorCode, List<Lesson>> tuple = schedulerService.querySchedulerByStudentNo("S0022320001");
        Assertions.assertEquals(tuple.getFirst(), ErrorCode.NORMAL);
        Assertions.assertEquals(tuple.getSecond().size(), 0);
    }

    @Test
    public void testQuerySchedulerNoCourseRecords() {
        Mockito.when(scRepository.findByStudentNo(Mockito.anyString())).thenReturn(null);

        Tuple<ErrorCode, List<Lesson>> tuple = schedulerService.querySchedulerByStudentNo("S0022320002");
        Assertions.assertEquals(tuple.getFirst(), ErrorCode.COURSE_RELATION_QUERY_NOT_EXISTS);
    }

    @Test
    public void testQueryFullScheduler() {
        Mockito.when(schedulerRepository.findAll()).thenReturn(new ArrayList<>());
        Mockito.when(courseRepository.findAll()).thenReturn(new ArrayList<>());

        Tuple<ErrorCode, List<Lesson>> tuple = schedulerService.queryFullScheduler();
        Assertions.assertEquals(tuple.getFirst(), ErrorCode.NORMAL);
        Assertions.assertEquals(tuple.getSecond().size(), 0);
    }

    @Test
    public void testUpdateScheduler() {
        List<Lesson> lessons = new ArrayList<>();

        Lesson lesson1 = new Lesson();
        lesson1.setDayOfWeek(9);
        lessons.add(lesson1);

        Lesson lesson2 = new Lesson();
        lesson2.setLessonNo(0);
        lessons.add(lesson2);

        Lesson lesson3 = new Lesson();
        lesson3.setStartWeek(0);
        lessons.add(lesson3);

        Lesson lesson4 = new Lesson();
        lesson3.setEndWeek(0);
        lessons.add(lesson4);

        Lesson lesson5 = new Lesson();
        lesson3.setStartWeek(10);
        lesson3.setEndWeek(0);
        lessons.add(lesson5);

        Mockito.when(schedulerRepository.saveAll(Mockito.anyList())).thenReturn(new ArrayList<>());

        ErrorCode errCode = schedulerService.updateScheduler(lessons);
        Assertions.assertEquals(errCode, ErrorCode.NORMAL);
    }
}
