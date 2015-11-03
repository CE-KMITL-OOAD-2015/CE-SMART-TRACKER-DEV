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
        else if(course.isEnrolled(student))
        {
            return new EnrollCourseStatus(EnrollCourseStatusEnum.ALREADY_ENROLLED);
        }
        else
        {
            course.enroll(student);
            courseRepository.save(course);
            return new EnrollCourseStatus(StatusEnum.SUCCESS);
        }
    }

    @RequestMapping("/viewRating")
    @ResponseBody
    public ViewRatingResponse requestViewRating(@RequestParam(value="courseId", required=true) String courseId)
    {
        return viewRating(courseId);
    }

    private ViewRatingResponse viewRating(String courseId)
    {
        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course == null)
        {
            return new ViewRatingResponse(ViewRatingResponseEnum.COURSE_NOT_FOUND);
        }
        else
        {
            ViewRatingResponse viewRatingResponse = new ViewRatingResponse(StatusEnum.SUCCESS);
            viewRatingResponse.setAverageRating(course.getAverageRating());
            return viewRatingResponse;
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
        return courseRepository.findByCourseNameContainingOrCourseIdContainingOrderByCourseNameAsc(keyword, keyword);
    }

    @RequestMapping("/rateCourse")
    @ResponseBody
    public RateCourseResponse requestRateCourse(@RequestParam(value="sessionId", required=true) String sessionId,
                                                @RequestParam(value="courseId", required=true) String courseId,
                                                @RequestParam(value="point", required=true) int point)
    {
        return rateCourse(sessionId, courseId, point);

    }

    private RateCourseResponse rateCourse(String sessionId, String courseId, int point)
    {
        Student student = studentRepository.findFirstBySessionId(sessionId);
        if(student == null)
        {
            return new RateCourseResponse(StatusEnum.INVALID_SESSION);
        }

        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course == null)
        {
            return new RateCourseResponse(RateCourseResponseEnum.COURSE_NOT_FOUND);
        }
        else if(!course.isEnrolled(student))
        {
            return new RateCourseResponse(RateCourseResponseEnum.NOT_ENROLLED);
        }
        /*
        else if(course.isRated(student))
        {
            //return new RateCourseResponse(RateCourseResponseEnum.ALREADY_RATED);
        }
        */
        else
        {
            course.addRating(student, point);
            courseRepository.save(course);
            return new RateCourseResponse(StatusEnum.SUCCESS);
        }
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

    @RequestMapping("/viewCourseDetail")
    @ResponseBody
    public Course requestCourseDetail(@RequestParam(value="courseId", required=true) String courseId)
    {
        return viewCourseDetail(courseId);
    }

    private Course viewCourseDetail(String courseId)
    {
        return courseRepository.findFirstByCourseId(courseId);
    }

    @RequestMapping("/addAssignment")
    @ResponseBody
    public AddAssignmentResponse requestAddAssignment(@RequestParam(value="sessionId", required=true) String sessionId,
                                                      @RequestParam(value="courseId", required=true) String courseId,
                                                      @RequestParam(value="title", required=true) String title,
                                                      @RequestParam(value="description", required=true) String description,
                                                      @RequestParam(value="maxScore", required=true) float maxScore)
    {
        return addAssignment(sessionId, courseId, title, description, maxScore);
    }


    private AddAssignmentResponse addAssignment(String sessionId, String courseId, String title, String description, float maxScore)
    {
        Teacher teacher = teacherRepository.findFirstBySessionId(sessionId);
        if(teacher == null)
        {
            return new AddAssignmentResponse(StatusEnum.INVALID_SESSION);
        }

        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course == null)
        {
            return new AddAssignmentResponse(AddAssignmentResponseEnum.COURSE_NOT_FOUND);
        }
        else if(!course.isTeaching(teacher))
        {
            return new AddAssignmentResponse(AddAssignmentResponseEnum.NOT_TEACHING);
        }
        else if(course.addAssignment(title, description, maxScore))
        {
            courseRepository.save(course);
            return new AddAssignmentResponse(StatusEnum.SUCCESS);
        }
        else
        {
            return new AddAssignmentResponse(AddAssignmentResponseEnum.DUPLICATED_TITLE);
        }

        //assignmentDescriptionRepository.save(assignmentDescription);

    }

    /*
    @RequestMapping("/changeScore")
    @ResponseBody
    public AddAssignmentResponse requestChangeScore(@RequestParam(value="sessionId", required=true) String sessionId,
                                                      @RequestParam(value="courseId", required=true) String courseId,
                                                      @RequestParam(value="title", required=true) String title,
                                                      @RequestParam(value="description", required=true) String description,
                                                      @RequestParam(value="maxScore", required=true) float maxScore)
    {
        return addAssignment(sessionId, courseId, title, description, maxScore);
    }
    */
}
