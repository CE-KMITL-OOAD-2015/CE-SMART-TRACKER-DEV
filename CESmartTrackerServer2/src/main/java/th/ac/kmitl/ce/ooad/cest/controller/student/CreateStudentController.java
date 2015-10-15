package th.ac.kmitl.ce.ooad.cest.controller.student;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.HibernateUtil;
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
        return saveStudent2(studentId, firstName, lastName, faculty, department);
    }

    CreateStudentStatus saveStudent2(String studentId, String firstName, String lastName, String faculty, String department)
    {
        CreateStudentStatus status = new CreateStudentStatus();
        if(studentId == null || firstName == null || lastName == null || faculty == null)
        {
            status.setMissingParameter();
            return status;
        }

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Student.class);
        cr.add(Restrictions.eq("studentId", studentId));
        List<Student> students = cr.list();
        if(students.isEmpty())
        {
            Student student = new Student();
            student.setStudentId(studentId);
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setFaculty(faculty);
            student.setDepartment(department);
            session.save(student);
            session.getTransaction().commit();
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
