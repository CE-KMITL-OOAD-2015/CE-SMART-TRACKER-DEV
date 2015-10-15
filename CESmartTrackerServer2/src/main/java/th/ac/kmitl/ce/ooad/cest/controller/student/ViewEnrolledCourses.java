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
import th.ac.kmitl.ce.ooad.cest.entity.Course;
import th.ac.kmitl.ce.ooad.cest.entity.Student;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/viewEnrolledCourses")
public class ViewEnrolledCourses {
    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody
    Set<Course> request(
            @RequestParam(value="studentId", required=true) String studentId) {
        return getEnrolledCourses(studentId);
    }

    Set<Course> getEnrolledCourses(String studentId)
    {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Student.class);
        cr.add(Restrictions.eq("studentId", studentId));
        List<Student> students = cr.list();
        if(students.isEmpty())
        {
            return (new HashSet<Course>());
        }
        else
        {
            Student student = students.get(0);
            Set<Course> courses = student.getEnrolledCourses();
            return courses;
        }
    }
}
