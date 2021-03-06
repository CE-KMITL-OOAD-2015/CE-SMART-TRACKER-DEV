package th.ac.kmitl.ce.ooad.cest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.domain.*;
import th.ac.kmitl.ce.ooad.cest.repository.CourseRepository;
import th.ac.kmitl.ce.ooad.cest.repository.StudentRepository;
import th.ac.kmitl.ce.ooad.cest.repository.TeacherRepository;
import th.ac.kmitl.ce.ooad.cest.service.response.*;


import java.time.DayOfWeek;
import java.util.*;

@Controller
public class CourseController
{

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;
    /*@Autowired
    private Validator validator;*/

    @RequestMapping("/createCourse")
    @ResponseBody
    public Response requestCreateCourse(@RequestParam(value="sessionId", required=true) String sessionId,
                                      @RequestParam(value="courseId", required=true) String courseId,
                                      @RequestParam(value="courseName", required=true) String courseName,
                                      @RequestParam(value="faculty", required=true) Faculty faculty,
                                      @RequestParam(value="description", required=false, defaultValue = "") String description,
                                      @RequestParam(value="department", required=false, defaultValue = "") String department,
                                      @RequestParam(value="courseDay", required=true) DayOfWeek courseDay,
                                      @RequestParam(value="courseTime", required=false) String courseTime)
    {
        return createCourse(sessionId.trim(), courseId.trim(), courseName.trim(), faculty, description.trim(), department.trim(), courseDay, courseTime.trim());

    }

    private Response createCourse(String sessionId, String courseId, String courseName, Faculty faculty, String description, String department, DayOfWeek courseDay, String courseTime)
    {
        Teacher teacher = teacherRepository.findFirstBySessionId(sessionId);
        if(teacher == null)
        {
            return new Response(ResponseEnum.INVALID_SESSION);
        }
        else if(courseRepository.findFirstByCourseId(courseId) != null)
        {
            return new Response(ResponseEnum.DUPLICATED_COURSE_ID);
        }
        else if(courseRepository.findFirstByCourseName(courseName) != null)
        {
            return new Response(ResponseEnum.DUPLICATED_COURSE_NAME);
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
            courseRepository.save(course);
            course = courseRepository.findFirstByCourseId(courseId);
            course.getTeachers().add(teacher);
            courseRepository.save(course);
            return new Response(ResponseEnum.SUCCESS);
        }
    }

    @RequestMapping("/enrollCourse")
    @ResponseBody
    public Response requestEnrollCourse(@RequestParam(value="sessionId", required=true) String sessionId,
                                        @RequestParam(value="courseId", required=true) String courseId)
    {
        return enrollCourse(sessionId.trim(), courseId.trim());
    }

    private Response enrollCourse(String sessionId, String courseId)
    {
        Student student = studentRepository.findFirstBySessionId(sessionId);
        Course course = courseRepository.findFirstByCourseId(courseId);
        if(student == null)
        {
            return new Response(ResponseEnum.INVALID_SESSION);
        }
        else if(course == null)
        {
            return new Response(ResponseEnum.COURSE_NOT_FOUND);
        }
        else if(course.isEnrolledBy(student))
        {
            return new Response(ResponseEnum.ALREADY_ENROLLED);
        }
        else
        {
            course.enroll(student);
            courseRepository.save(course);
            return new Response(ResponseEnum.SUCCESS);
        }
    }

    @RequestMapping("/dropCourse")
    @ResponseBody
    public Response requestDropCourse(@RequestParam(value="sessionId", required=true) String sessionId,
                                      @RequestParam(value="courseId", required=true) String courseId)
    {
        return dropCourse(sessionId.trim(), courseId.trim());
    }

    private Response dropCourse(String sessionId, String courseId)
    {
        Student student = studentRepository.findFirstBySessionId(sessionId);
        if(student == null)
        {
            return new Response(ResponseEnum.INVALID_SESSION);
        }

        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course == null)
        {
            return new Response(ResponseEnum.COURSE_NOT_FOUND);
        }
        else if(!course.isEnrolledBy(student))
        {
            return new Response(ResponseEnum.NOT_ENROLLED);
        }
        else
        {
            course.drop(student);
            courseRepository.save(course);
            return new Response(ResponseEnum.SUCCESS);
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

    @RequestMapping("/findCourseByFaculty")
    @ResponseBody
    public List<Course> requestFindCourseByFaculty(@RequestParam(value="faculty", required=true) Faculty faculty)
    {
        return findCourseByFaculty(faculty);
    }

    private List<Course> findCourseByFaculty(Faculty faculty)
    {
        return courseRepository.findByFacultyOrderByCourseNameAsc(faculty);
    }

    @RequestMapping("/findCourseByDepartment")
    @ResponseBody
    public List<Course> requestFindCourseByDepartment(@RequestParam(value="department", required=false, defaultValue="") String department)
    {
        return findCourseByDepartment(department);
    }

    private List<Course> findCourseByDepartment(String department)
    {
        return courseRepository.findByDepartmentOrderByCourseNameAsc(department);
    }

    @RequestMapping("/findRecommendedCourse")
    @ResponseBody
    public List<Course> requestFindRecommendedCourse(@RequestParam(value="sessionId", required=true) String sessionId)
    {
        return findRecommendedCourse(sessionId);
    }

    private List<Course> findRecommendedCourse(String sessionId)
    {
        Student student = studentRepository.findFirstBySessionId(sessionId);
        if(student != null)
        {
            return courseRepository.findByFacultyAndDepartmentOrNone(student.getFaculty(), student.getDepartment());
        }
        else
        {
            return new ArrayList<Course>(); // return empty list
        }
    }

    @RequestMapping("/viewEnrolledStudents")
    @ResponseBody
    public Set<Student> requestViewEnrolledStudents(@RequestParam(value="courseId", required=true) String courseId)
    {
        return viewEnrolledStudents(courseId.trim());
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

    @RequestMapping("/viewTeachers")
    @ResponseBody
    public Set<Teacher> requestViewTeachers(@RequestParam(value="courseId", required=true) String courseId)
    {
        return viewTeachers(courseId.trim());
    }


    private Set<Teacher> viewTeachers(String courseId)
    {
        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course != null)
        {
            return course.getTeachers();
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
        return viewCourseDetail(courseId.trim());
    }

    private Course viewCourseDetail(String courseId)
    {
        return courseRepository.findFirstByCourseId(courseId);
    }

    @RequestMapping("/viewReport")
    @ResponseBody
    public ViewReportResponse requestViewReport(@RequestParam(value="sessionId", required=true) String sessionId,
                                                @RequestParam(value="courseId", required=true) String courseId)
    {
        return viewReport(sessionId.trim(), courseId.trim());
    }

    private ViewReportResponse viewReport(String sessionId, String courseId)
    {
        Teacher teacher = teacherRepository.findFirstBySessionId(sessionId);
        Course course = courseRepository.findFirstByCourseId(courseId);
        if(teacher == null)
        {
            return new ViewReportResponse(ResponseEnum.INVALID_SESSION);
        }
        else if(course == null)
        {
            return new ViewReportResponse(ResponseEnum.COURSE_NOT_FOUND);
        }
        if(!course.isTeachingBy(teacher))
        {
            return new ViewReportResponse(ResponseEnum.NOT_TEACHING);
        }

        return new ViewReportResponse(ResponseEnum.SUCCESS, course);
    }

    @RequestMapping("/test")
    @ResponseBody
    public List<Course> requestTest(@RequestParam(value = "text", required = false)String text)
    {
        return courseRepository.findByFacultyAndDepartmentOrNone(Faculty.ENGINEERING, "computer");
    }

    @RequestMapping("/courseSchedule")
    public String requestCourseSchedule(@RequestParam(value="username", required=false) String username, Model model)
    {
        if(username == null)
        {
            return "notFound";
        }
        Student student = studentRepository.findFirstByUsername(username);
        if(student == null)
        {
            return "notFound";
        }
        else
        {
            List<Course> courses = new ArrayList<>(student.getEnrolledCourses());
            Collections.sort(courses, new Comparator<Course>()
            {
                @Override
                public int compare(Course o1, Course o2)
                {
                    return o1.getCourseDay().compareTo(o2.getCourseDay());
                }
            });
            model.addAttribute("username", username);
            model.addAttribute("courses", courses);
            return "schedule";
        }

    }


    private class ViewReportResponse extends Response
    {
        private Course course;
        private Set<Teacher> teachers;
        private List<ScoreBook> assignmentBooks;
        private Statistic statistic;

        public ViewReportResponse(ResponseEnum responseEnum)
        {
            super(responseEnum);
        }

        public ViewReportResponse(ResponseEnum responseEnum, Course course)
        {
            super(responseEnum);
            this.course = course;
            this.teachers = course.getTeachers();
            this.assignmentBooks = new ArrayList<>(course.getScoreBooks());
            Collections.sort(this.assignmentBooks);
            this.statistic = course.getStatistic();
        }

        public Course getCourse()
        {
            return course;
        }

        public Set<Teacher> getTeachers()
        {
            return teachers;
        }

        public List<ScoreBook> getAssignmentBooks()
        {
            return assignmentBooks;
        }

        public Statistic getStatistic()
        {
            return statistic;
        }
    }

}
