package th.ac.kmitl.ce.ooad.cest.controller.student;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.config.AppConfig;
import th.ac.kmitl.ce.ooad.cest.dao.student.IStudentDao;
import th.ac.kmitl.ce.ooad.cest.entity.Student;
import th.ac.kmitl.ce.ooad.cest.status.CreateStudentStatus;

import java.util.List;

@Controller
@RequestMapping("/createStudent")
public class CreateStudentController {
    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody
    CreateStudentStatus request(
            @RequestParam(value="studentId", required=false) String studentId,
            @RequestParam(value="firstName", required=false) String firstName,
            @RequestParam(value="lastName", required=false) String lastName,
            @RequestParam(value="faculty", required=false) String faculty,
            @RequestParam(value="department", required=false) String department) {
        return saveStudent(studentId, firstName, lastName, faculty, department);
    }
    
    CreateStudentStatus saveStudent(String studentId, String firstName, String lastName, String faculty, String department)
    {
        CreateStudentStatus status = new CreateStudentStatus();
        if(studentId == null || firstName == null || lastName == null || faculty == null || department == null)
        {
            status.setMissingParameter();
            return status;
        }

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        IStudentDao edao = ctx.getBean(IStudentDao.class);

        List<Student> students = edao.findByStudentId(studentId);
        if(students.isEmpty())
        {
            Student student = new Student();
            student.setStudentId(studentId);
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setFaculty(faculty);
            student.setDepartment(department);
            edao.saveStudent(student);

            status.setSuccess();
            return status;
        }
        else
        {
            status.setDuplicatedStudentId();
            return status;
        }
    }
}
