package th.ac.kmitl.ce.ooad.cest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.domain.Course;
import th.ac.kmitl.ce.ooad.cest.domain.Faculty;
import th.ac.kmitl.ce.ooad.cest.domain.Teacher;
import th.ac.kmitl.ce.ooad.cest.repository.CourseRepository;
import th.ac.kmitl.ce.ooad.cest.repository.StudentRepository;
import th.ac.kmitl.ce.ooad.cest.repository.TeacherRepository;
import th.ac.kmitl.ce.ooad.cest.repository.UserRepository;
import th.ac.kmitl.ce.ooad.cest.service.response.Response;
import th.ac.kmitl.ce.ooad.cest.service.response.ResponseEnum;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

@Controller
public class TeacherController
{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;
    /*@Autowired
    private Validator validator;*/

    @RequestMapping("/createTeacher")
    @ResponseBody
    public Response requestCreateTeacher(@RequestParam(value = "username", required = true) String username,
                                         @RequestParam(value = "password", required = true) String password,
                                         @RequestParam(value = "firstName", required = true) String firstName,
                                         @RequestParam(value = "lastName", required = true) String lastName,
                                         @RequestParam(value = "faculty", required = true) Faculty faculty,
                                         @RequestParam(value = "department", required = false, defaultValue = "") String department,
                                         @RequestParam(value = "facebookId", required = false, defaultValue = "") String facebookId)
    {
        return createTeacher(username.trim(), password, firstName.trim(), lastName.trim(), faculty, department.trim(), facebookId.trim());

    }

    private Response createTeacher(String username, String password, String firstName, String lastName, Faculty faculty, String department, String facebookId)
    {

        if(userRepository.findFirstByUsername(username) != null)
        {
            return new Response(ResponseEnum.DUPLICATED_USERNAME);
        }
        else if(!facebookId.equals("") && userRepository.findFirstByFacebookId(facebookId) != null)
        {
            return new Response(ResponseEnum.DUPLICATED_FACEBOOK_ID);
        }
        else
        {
            try
            {
                String[] results = HashingUtil.hashPassword(password);
                Teacher teacher = new Teacher();
                teacher.setUsername(username);
                teacher.setPassword(results[0]);
                teacher.setSalt(results[1]);
                teacher.setFirstName(firstName);
                teacher.setLastName(lastName);
                teacher.setFaculty(faculty);
                teacher.setDepartment(department);
                teacher.setFacebookId(facebookId);
                teacherRepository.save(teacher);
                return new Response(ResponseEnum.SUCCESS);
            }
            catch(NoSuchAlgorithmException e)
            {
                return new Response(ResponseEnum.ERROR);
            }
            catch(UnsupportedEncodingException e)
            {
                return new Response(ResponseEnum.ERROR);
            }
        }

    }

    @RequestMapping("/viewTeachingCourses")
    @ResponseBody
    public ViewTeachingCoursesResponse requestViewTeachingCourses(@RequestParam(value = "sessionId", required = true) String sessionId)
    {
        return viewTeachingCourses(sessionId.trim());
    }

    private ViewTeachingCoursesResponse viewTeachingCourses(String sessionId)
    {
        Teacher teacher = teacherRepository.findFirstBySessionId(sessionId);
        if(teacher != null)
        {
            ViewTeachingCoursesResponse viewTeachingCoursesResponse = new ViewTeachingCoursesResponse(ResponseEnum.SUCCESS);
            viewTeachingCoursesResponse.setTeachingCourses(teacher.getTeachingCourses());
            return viewTeachingCoursesResponse;
        }
        else
        {
            return new ViewTeachingCoursesResponse(ResponseEnum.INVALID_SESSION);
        }
    }

    private class ViewTeachingCoursesResponse extends Response
    {
        private Set<Course> teachingCourses;

        public Set<Course> getTeachingCourses()
        {
            return teachingCourses;
        }

        public void setTeachingCourses(Set<Course> teachingCourses)
        {
            this.teachingCourses = teachingCourses;
        }

        public ViewTeachingCoursesResponse(ResponseEnum responseEnum)
        {
            super(responseEnum);
        }
    }
}
