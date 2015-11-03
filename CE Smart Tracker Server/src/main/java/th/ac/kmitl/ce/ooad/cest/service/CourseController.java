package th.ac.kmitl.ce.ooad.cest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.domain.Course;
import th.ac.kmitl.ce.ooad.cest.domain.Student;
import th.ac.kmitl.ce.ooad.cest.domain.Teacher;
import th.ac.kmitl.ce.ooad.cest.repository.CourseRepository;
import th.ac.kmitl.ce.ooad.cest.repository.StudentRepository;
import th.ac.kmitl.ce.ooad.cest.repository.TeacherRepository;
import th.ac.kmitl.ce.ooad.cest.service.response.*;

import java.util.List;
import java.util.Set;

@Controller
public class CourseController
{

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    CourseRepository courseRepository;

    @RequestMapping("/createCourse")
    @ResponseBody
    public Status requestCreateCourse(@RequestParam(value="sessionId", required=true) String sessionId,
                                      @RequestParam(value="courseId", required=true) String courseId,
                                      @RequestParam(value="courseName", required=true) String courseName,
                                      @RequestParam(value="faculty", required=true) String faculty,
                                      @RequestParam(value="description", required=false) String description,
                                      @RequestParam(value="department", required=false) String department,
                                      @RequestParam(value="courseDay", required=false) String courseDay,
                                      @RequestParam(value="courseTime", required=false) String courseTime)
    {
        return createCourse(sessionId, courseId, courseName, faculty, description, department, courseDay, courseTime);

    }

    private Status createCourse(String sessionId, String courseId, String courseName, String faculty, String description, String department, String courseDay, String courseTime)
    {
        Teacher teacher = teacherRepository.findFirstBySessionId(sessionId);
        if(teacher == null)
        {
            return new Status(StatusEnum.INVALID_SESSION);
        }
        else if(courseRepository.findFirstByCourseId(courseId) != null)
        {
            return new CreateCourseStatus(CreateCourseStatusEnum.DUPLICATED_COURSE_ID);
        }
        else
        {
            Course course = new Course();
            course.setCourseId(courseId);
            course.setCourseName(courseName);
            course.setFaculty(faculty);
            course.setDepartment(department);
            course.setDescription(description);
            course.setCourseDay(courseDay);
            course.setCourseTime(courseTime);
            course.getTeachers().add(teacher);
            courseRepository.save(course);
            return new CreateCourseStatus(StatusEnum.SUCCESS);
        }
    }

    @RequestMapping("/enrollCourse")
    @ResponseBody
    public EnrollCourseStatus requestEnrollCourse(@RequestParam(value="sessionId", required=true) String sessionId,
                                                  @RequestParam(value="courseId", required=true) String courseId)
    {
        return enrollCourse(sessionId, courseId);
    }

    private EnrollCourseStatus enrollCourse(String sessionId, String courseId)
    {
        Student student = studentRepository.findFirstBySessionId(sessionId);
        Course course = courseRepository.findFirstByCourseId(courseId);
        if(student == null)
        {
            return new EnrollCourseStatus(StatusEnum.INVALID_SESSION);
        }
        else if(course == null)
        {
            return new EnrollCourseStatus(EnrollCourseStatusEnum.COURSE_NOT_FOUND);
        }
        else
        {
            course.enroll(student);
            courseRepository.save(course);
            return new EnrollCourseStatus(StatusEnum.SUCCESS);
        }
    }

    @RequestMapping("/findCourse")
    @ResponseBody
    public List<Course> requestFindCourse(@RequestParam(value="keyword", required=false, defaultValue="") String keyword)
    {
        return findCourse(keyword);

    }


    private List<Course> findCourse(String keyword)
    {
        return courseRepository.findByCourseNameContainingOrCourseIdContaining(keyword, keyword);
    }

    @RequestMapping("/viewEnrolledStudents")
    @ResponseBody
    public Set<Student> requestViewEnrolledStudents(@RequestParam(value="courseId", required=true) String courseId)
    {
        return viewEnrolledStudents(courseId);
    }


    private Set<Student> viewEnrolledStudents(String courseId)
    {
        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course != null)
        {
            return course.getEnrolledStudents();
        }
        else
        {
            return null;
        }
    }
}
