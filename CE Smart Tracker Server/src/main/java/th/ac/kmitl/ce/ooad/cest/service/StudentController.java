package th.ac.kmitl.ce.ooad.cest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.domain.Course;
import th.ac.kmitl.ce.ooad.cest.domain.Faculty;
import th.ac.kmitl.ce.ooad.cest.domain.Student;
import th.ac.kmitl.ce.ooad.cest.repository.CourseRepository;
import th.ac.kmitl.ce.ooad.cest.repository.StudentRepository;
import th.ac.kmitl.ce.ooad.cest.repository.TeacherRepository;
import th.ac.kmitl.ce.ooad.cest.repository.UserRepository;
import th.ac.kmitl.ce.ooad.cest.service.response.Response;
import th.ac.kmitl.ce.ooad.cest.service.response.ResponseEnum;
import th.ac.kmitl.ce.ooad.cest.util.HashingUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@Controller
public class StudentController
{

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/createStudent")
    @ResponseBody
    public Response requestCreateStudent(@RequestParam(value = "username", required = true) String username,
                                         @RequestParam(value = "password", required = true) String password,
                                         @RequestParam(value = "studentId", required = true) int studentId,
                                         @RequestParam(value = "firstName", required = true) String firstName,
                                         @RequestParam(value = "lastName", required = true) String lastName,
                                         @RequestParam(value = "faculty", required = true) Faculty faculty,
                                         @RequestParam(value = "department", required = true) String department,
                                         @RequestParam(value = "facebookId", required = false, defaultValue = "") String facebookId)
    {
        return createStudent(username.trim(), password, studentId, firstName.trim(), lastName.trim(), faculty, department.trim(), facebookId.trim());

    }

    @RequestMapping("/viewEnrolledCourses")
    @ResponseBody
    public ViewEnrolledCoursesResponse requestViewEnrolledCourses(@RequestParam(value = "sessionId", required = true) String sessionId)
    {
        return viewEnrolledCourses(sessionId.trim());
    }

    private ViewEnrolledCoursesResponse viewEnrolledCourses(String sessionId)
    {
        Student student = studentRepository.findFirstBySessionId(sessionId);
        if (student != null)
        {
            ViewEnrolledCoursesResponse viewEnrolledCoursesResponse = new ViewEnrolledCoursesResponse(ResponseEnum.SUCCESS);
            viewEnrolledCoursesResponse.setEnrolledCourses(student.getEnrolledCourses());
            return viewEnrolledCoursesResponse;
        }
        else
        {
            return new ViewEnrolledCoursesResponse(ResponseEnum.INVALID_SESSION);
        }
    }

    private Response createStudent(String username, String password, int studentId, String firstName, String lastName, Faculty faculty, String department, String facebookId)
    {
        try
        {
            if (userRepository.findFirstByUsername(username) != null)
            {
                return new Response(ResponseEnum.DUPLICATED_USERNAME);
            }
            else if(userRepository.findFirstByFacebookId(facebookId) != null)
            {
                return new Response(ResponseEnum.DUPLICATED_FACEBOOK_ID);
            }
            else if (!studentRepository.findByStudentId(studentId).isEmpty())
            {
                return new Response(ResponseEnum.DUPLICATED_STUDENT_ID);
            }
            else
            {
                String[] results = HashingUtil.hashPassword(password);
                Student student = new Student();
                student.setUsername(username);
                student.setPassword(results[0]);
                student.setSalt(results[1]);
                student.setStudentId(studentId);
                student.setFirstName(firstName);
                student.setLastName(lastName);
                student.setFaculty(faculty);
                student.setDepartment(department);
                student.setFacebookId(facebookId);
                studentRepository.save(student);
                return new Response(ResponseEnum.SUCCESS);
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            return new Response(ResponseEnum.ERROR);
        }
        catch (UnsupportedEncodingException e)
        {
            return new Response(ResponseEnum.ERROR);
        }
    }

    private class ViewEnrolledCoursesResponse extends Response
    {
        private Set<Course> enrolledCourses;

        public Set<Course> getEnrolledCourses()
        {
            return enrolledCourses;
        }

        public void setEnrolledCourses(Set<Course> enrolledCourses)
        {
            this.enrolledCourses = enrolledCourses;
        }

        public ViewEnrolledCoursesResponse(ResponseEnum responseEnum)
        {
            super(responseEnum);
        }
    }
}
