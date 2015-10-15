package th.ac.kmitl.ce.ooad.cest.controller.course;

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
@RequestMapping("/viewEnrolledStudents")
public class ViewEnrolledStudents {

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody
    Set<Student> request(
            @RequestParam(value="courseId", required=true) String courseId) {
        return getEnrolledStudents(courseId);
    }

    public Set<Student> getEnrolledStudents(String courseId)
    {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Course.class);
        cr.add(Restrictions.eq("courseId", courseId));
        List<Course> courses = cr.list();
        if(courses.isEmpty())
        {
            return (new HashSet<Student>());
        }
        else
        {
            Course course = courses.get(0);
            Set<Student> students = course.getEnrolledStudents();
            return students;
        }
    }
}
