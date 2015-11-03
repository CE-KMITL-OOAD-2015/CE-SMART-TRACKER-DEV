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
import th.ac.kmitl.ce.ooad.cest.util.CredentialUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Controller
public class TeacherController {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    CourseRepository courseRepository;

    @RequestMapping("/createTeacher")
    @ResponseBody
    public CreateStudentStatus requestCreateStudent(@RequestParam(value="username", required=true) String username,
                                                       @RequestParam(value="password", required=true) String password,
                                                       @RequestParam(value="firstName", required=true) String firstName,
                                                       @RequestParam(value="lastName", required=true) String lastName,
                                                       @RequestParam(value="faculty", required=true) String faculty,
                                                       @RequestParam(value="department", required=false) String department)
    {
        return createTeacher(username, password, firstName, lastName, faculty, department);

    }

    private CreateStudentStatus createTeacher(String username, String password, String firstName, String lastName, String faculty, String department) {
        try
        {
            if(studentRepository.findFirstByUsername(username) != null || teacherRepository.findFirstByUsername(username) != null)
            {
                return new CreateStudentStatus(CreateStudentStatusEnum.DUPLICATED_USERNAME);
            }
            else
            {
                String[] results = CredentialUtil.hashPassword(password);
                Teacher teacher = new Teacher();
                teacher.setUsername(username);
                teacher.setPassword(results[0]);
                teacher.setSalt(results[1]);
                teacher.setFirstName(firstName);
                teacher.setLastName(lastName);
                teacher.setFaculty(faculty);
                teacherRepository.save(teacher);
                return new CreateStudentStatus(StatusEnum.SUCCESS);
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            return new CreateStudentStatus(StatusEnum.ERROR);
        }
        catch (UnsupportedEncodingException e)
        {
            return new CreateStudentStatus(StatusEnum.ERROR);
        }
    }

}
