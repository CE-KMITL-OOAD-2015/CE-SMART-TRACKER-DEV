package th.ac.kmitl.ce.ooad.cest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.domain.Student;
import th.ac.kmitl.ce.ooad.cest.repository.AccountRepository;
import th.ac.kmitl.ce.ooad.cest.repository.CourseRepository;
import th.ac.kmitl.ce.ooad.cest.repository.StudentRepository;
import th.ac.kmitl.ce.ooad.cest.repository.TeacherRepository;
import th.ac.kmitl.ce.ooad.cest.service.response.*;
import th.ac.kmitl.ce.ooad.cest.util.CredentialUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Controller
public class StudentController {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    AccountRepository accountRepository;

    @RequestMapping("/createStudent")
    @ResponseBody
    public CreateStudentStatus requestCreateStudent(@RequestParam(value="username", required=true) String username,
                   @RequestParam(value="password", required=true) String password,
                   @RequestParam(value="studentId", required=true) int studentId,
                   @RequestParam(value="firstName", required=true) String firstName,
                   @RequestParam(value="lastName", required=true) String lastName,
                   @RequestParam(value="faculty", required=true) String faculty,
                   @RequestParam(value="department", required=false) String department)
    {
        return createUser(username, password, studentId, firstName, lastName, faculty, department);

    }

    private CreateStudentStatus createUser(String username, String password, int studentId, String firstName, String lastName, String faculty, String department) {
        try
        {
            //if(studentRepository.findFirstByUsername(username) != null || teacherRepository.findFirstByUsername(username) != null)
            if(accountRepository.findFirstByUsername(username) != null)
            {
                return new CreateStudentStatus(CreateStudentStatusEnum.DUPLICATED_USERNAME);
            }
            else if(!studentRepository.findByStudentId(studentId).isEmpty())
            {
                return new CreateStudentStatus(CreateStudentStatusEnum.DUPLICATED_STUDENT_ID);
            }
            else
            {
                String[] results = CredentialUtil.hashPassword(password);
                Student student = new Student();
                student.setUsername(username);
                student.setPassword(results[0]);
                student.setSalt(results[1]);
                student.setStudentId(studentId);
                student.setFirstName(firstName);
                student.setLastName(lastName);
                student.setFaculty(faculty);
                studentRepository.save(student);
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
