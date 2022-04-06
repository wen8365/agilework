package com.agilework.sims.service;

import com.agilework.sims.dto.Lesson;
import com.agilework.sims.entity.Course;
import com.agilework.sims.entity.CourseScheduler;
import com.agilework.sims.repository.CourseRepository;
import com.agilework.sims.repository.SchedulerRepository;
import com.agilework.sims.repository.StuCourseRelationshipRepository;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.SLogger;
import com.agilework.sims.util.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulerService {
    private static final String TAG = "SchedulerService";

    @Autowired
    private StuCourseRelationshipRepository scRepository;
    @Autowired
    private SchedulerRepository schedulerRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Value("${scheduler.week.start}")
    private int weekStart;
    @Value("${scheduler.week.end}")
    private int weekEnd;
    @Value("${scheduler.lesson.start}")
    private int lessonStart;
    @Value("${scheduler.lesson.end}")
    private int lessonEnd;

    public Tuple<ErrorCode, List<Lesson>> querySchedulerByStudentNo(String studentNo) {
        SLogger.info(TAG, "query scheduler by studentNo: " + studentNo);
        List<String> courseNos = scRepository.findByStudentNo(studentNo);
        if (courseNos == null || courseNos.isEmpty()) {
            return new Tuple<>(ErrorCode.COURSE_RELATION_QUERY_NOT_EXISTS, null);
        }
        List<CourseScheduler> schedulers = schedulerRepository.findByCourseNoIn(courseNos);
        List<Course> courses = courseRepository.findByCourseNoIn(courseNos);
        List<Lesson> lessons = convertToLesson(schedulers, courses);

        SLogger.info(TAG, "schedulers.size=" + schedulers.size() + ", courses.size=" + courses.size()
                + ", lessons.size=" + lessons.size());
        return new Tuple<>(ErrorCode.NORMAL, lessons);
    }

    public Tuple<ErrorCode, List<Lesson>> queryFullScheduler() {
        SLogger.info(TAG, "query full scheduler");
        List<CourseScheduler> schedulers = schedulerRepository.findAll();
        List<Course> courses = courseRepository.findAll();
        List<Lesson> lessons = convertToLesson(schedulers, courses);

        SLogger.info(TAG, "schedulers.size=" + schedulers.size() + ", courses.size=" + courses.size()
                + ", lessons.size=" + lessons.size());
        return new Tuple<>(ErrorCode.NORMAL, lessons);
    }

    private List<Lesson> convertToLesson(List<CourseScheduler> schedulerList, List<Course> courses) {
        List<Lesson> lessons = new ArrayList<>();
        for (CourseScheduler scheduler : schedulerList) {
            Lesson lesson = new Lesson();
            lesson.setDayOfWeek(scheduler.getDayOfWeek());
            lesson.setLessonNo(scheduler.getLessonNo());
            lesson.setCourseNo(scheduler.getCourseNo());
            lesson.setAddress(scheduler.getAddress());
            lesson.setStartWeek(scheduler.getStartWeek());
            lesson.setEndWeek(scheduler.getEndWeek());

            for (Course course : courses) {
                if (course.getCourseNo().equals(scheduler.getCourseNo())) {
                    lesson.setCourseName(course.getCourseName());
                }
            }
            lessons.add(lesson);
        }
        return lessons;
    }

    public ErrorCode updateScheduler(List<Lesson> lessons) {
        SLogger.info(TAG, "update scheduler, lessons.size=" + lessons.size());
        List<CourseScheduler> schedulerList = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (isLessonInvalid(lesson)) continue;
            CourseScheduler scheduler = new CourseScheduler();
            scheduler.setDayOfWeek(lesson.getDayOfWeek());
            scheduler.setLessonNo(lesson.getLessonNo());
            scheduler.setCourseNo(lesson.getCourseNo());
            scheduler.setAddress(lesson.getAddress());
            scheduler.setStartWeek(lesson.getStartWeek());
            scheduler.setEndWeek(lesson.getEndWeek());
            schedulerList.add(scheduler);
        }
        List<CourseScheduler> result = schedulerRepository.saveAll(schedulerList);
        SLogger.info(TAG, "update scheduler end, result.size=" + result.size());
        return ErrorCode.NORMAL;
    }

    private boolean isLessonInvalid(Lesson lesson) {
        return lesson.getDayOfWeek() < 1 || lesson.getDayOfWeek() > 7
                || lesson.getLessonNo() < lessonStart || lesson.getLessonNo() > lessonEnd
                || lesson.getStartWeek() < weekStart || lesson.getEndWeek() > weekEnd
                || lesson.getStartWeek() > lesson.getEndWeek();
    }
}
