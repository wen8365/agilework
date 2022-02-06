package com.agilework.sims.service;

import com.agilework.sims.dto.StudentInfo;
import com.agilework.sims.entity.Student;
import com.agilework.sims.entity.StudentV;
import com.agilework.sims.repository.FastSaveRepositoryImpl;
import com.agilework.sims.repository.StudentRepository;
import com.agilework.sims.repository.StudentVRepository;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.SLogger;
import com.agilework.sims.util.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final String TAG = "StudentService";

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FastSaveRepositoryImpl<Student> saveService;

    @Autowired
    private StudentVRepository studentVRepository;

    @Value("${user.status.normal}")
    private int normalStatus;

    @Value("${user.status.deleted}")
    private int delStatus;

    public Tuple<ErrorCode, String> importStudents(List<StudentInfo> studentInfoList) {
        List<Student> students = new ArrayList<>();
        convertToStudents(studentInfoList, students);
        List<Student> result;
        try {
           result = saveService.insertAll(students);
        } catch (DataIntegrityViolationException e) {
            String cause = Objects.requireNonNull(e.getRootCause()).toString();
            SLogger.error(TAG, "import students ERROR:" + cause);
            int start = cause.indexOf("entry '") + 7;
            int end = cause.indexOf("' for key");
            String duplicateNo = start > -1 && end > -1 && start < end ? cause.substring(start, end) : "";
            return new Tuple<>(ErrorCode.STUDENT_IMPORT_USER_ALREADY_EXISTS, duplicateNo);
        }
        SLogger.info(TAG, "import student complete, total=" + students.size() + ", result=" + result.size());
        return new Tuple<>(ErrorCode.NORMAL, null);
    }

    private void convertToStudents(List<StudentInfo> studentInfoList, List<Student> students) {
        for (StudentInfo studentInfo : studentInfoList) {
            students.add(new Student(studentInfo));
        }
    }

    private StudentInfo convertToStudentInfo(StudentV student) {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setStudentNo(student.getStudentNo());
        studentInfo.setStudentName(student.getStudentName());
        studentInfo.setSex(student.getSex());
        studentInfo.setMajor(student.getMajor());
        studentInfo.setGrade(student.getGrade());
        studentInfo.setClazz(student.getClazz());
        return studentInfo;
    }

    public StudentInfo queryStudent(String userNo) {
        StudentV student =  studentVRepository.findByStudentNo(userNo);
        if (student != null) {
            SLogger.info(TAG, "query SUCCESS, studentNo= " + student.getStudentNo());
            return convertToStudentInfo(student);
        }
        SLogger.error(TAG, "query FAILED, student NOT FOUND!");
        return null;
    }

    public Page<StudentV> queryStudents(StudentInfo studentInfo, int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Specification<StudentV> specification = createSpecification(studentInfo);
        Page<StudentV> pages = studentVRepository.findAll(specification, pageable);
        SLogger.info(TAG, "query SUCCESS, size=" + pages.getSize()
                + ", page=" + pages.getNumber() + "/" + pages.getTotalPages());
        return pages;
    }

    private Specification<StudentV> createSpecification(final StudentInfo studentInfo) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate studentNoPredicate = null, studentNamePredicate = null;
            List<Predicate> list = new ArrayList<>();
            Path<String> studentNo = root.get("studentNo");
            Path<String> studentName = root.get("studentName");
            Path<String> sex = root.get("sex");
            Path<String> major = root.get("major");
            Path<Integer> grade = root.get("grade");
            Path<Integer> clazz = root.get("clazz");
            
            System.out.println(studentInfo);

            if (studentInfo.getStudentNo() != null) {
            	studentNoPredicate = criteriaBuilder.like(studentNo, "%"+ studentInfo.getStudentNo() + "%");
            }
            if (studentInfo.getStudentName() != null) {
            	studentNamePredicate = criteriaBuilder.like(studentName, "%"+ studentInfo.getStudentName() + "%");
            }
            if (studentInfo.getSex() != null) {
                Predicate p = criteriaBuilder.equal(sex, studentInfo.getSex());
                list.add(p);
            }
            if (studentInfo.getMajor() != null) {
                Predicate p = criteriaBuilder.like(major, studentInfo.getMajor());
                list.add(p);
            }
            if (studentInfo.getGrade() != null) {
                Predicate p = criteriaBuilder.equal(grade, studentInfo.getGrade());
                list.add(p);
            }
            if (studentInfo.getClazz() != null) {
                Predicate p = criteriaBuilder.equal(clazz, studentInfo.getClazz());
                list.add(p);
            }
            return criteriaBuilder.and(
            	criteriaBuilder.or(studentNoPredicate, studentNamePredicate), 
            	list.size()>0 ? criteriaBuilder.and(list.toArray(new Predicate[0])) : 
                		criteriaBuilder.isTrue(criteriaBuilder.literal(true))
            );
        };
    }

    public ErrorCode updateStudent(StudentInfo studentInfo) {
        Student student = new Student();
        student.copy(studentInfo);
        int row = studentRepository.updateByStudentNoAndStatus(student, normalStatus);
        ErrorCode code = row > 0 ? ErrorCode.NORMAL : ErrorCode.STUDENT_QUERY_NOT_EXISTS;
        SLogger.info(TAG, "update complete, row=" + row + ", reason=" + code.getReason());
        return code;
    }

    public Tuple<ErrorCode, Integer> updateStudents(List<StudentInfo> studentInfoList) {
        int rows = 0;
        for (StudentInfo studentInfo : studentInfoList) {
            Student student = new Student();
            student.copy(studentInfo);
            rows += studentRepository.updateByStudentNoAndStatus(student, normalStatus);
        }
        SLogger.info(TAG, "update complete, row=" + rows);
        return new Tuple<>(ErrorCode.NORMAL, rows);
    }

    public Tuple<ErrorCode, Integer> removeStudents(List<String> studentNoList) {
        int rows = 0;
        for (String studentNo : studentNoList) {
            rows += studentRepository.updateStudentStatus(studentNo, delStatus);
        }
        SLogger.info(TAG, "remove complete, row=" + rows);
        return new Tuple<>(ErrorCode.NORMAL, rows);
    }
    
    public Map<String, List<?>> studentConditionSelect() {
    	List<StudentV> studentVs=studentVRepository.findAll();
    	Map<String, List<?>> map=new HashMap<String, List<?>>();
    	
    	List<String> sexes=studentVs.stream().map(StudentV::getSex).distinct().sorted()
    		.collect(Collectors.toList());
    	map.put("sexes", sexes);
    	
    	List<String> majors=studentVs.stream().map(StudentV::getMajor).distinct().sorted()
    		.collect(Collectors.toList());
    	map.put("majors", majors);
    	
    	List<Integer> grades=studentVs.stream().map(StudentV::getGrade).distinct().sorted()
        	.collect(Collectors.toList());
    	map.put("grades", grades);
    	
    	List<Integer> classes=studentVs.stream().map(StudentV::getClazz).distinct().sorted()
    		.collect(Collectors.toList());
    	map.put("classes", classes);
    	
    	return map;
    }
}
