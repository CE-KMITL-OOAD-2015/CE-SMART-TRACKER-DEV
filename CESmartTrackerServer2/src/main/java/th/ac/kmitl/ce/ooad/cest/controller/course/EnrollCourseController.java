package th.ac.kmitl.ce.ooad.cest.controller.course;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.HibernateUtil;
import th.ac.kmitl.ce.ooad.cest.config.AppConfig;
import th.ac.kmitl.ce.ooad.cest.dao.course.ICourseDao;
import th.ac.kmitl.ce.ooad.cest.dao.student.IStudentDao;
import th.ac.kmitl.ce.ooad.cest.entity.Course;
import th.ac.kmitl.ce.ooad.cest.entity.Student;
import th.ac.kmitl.ce.ooad.cest.status.Status;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

@Controller
@RequestMapping("/enrollCourse")
public class EnrollCourseController {

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody
    Status request(
            @RequestParam(value="courseId", required=false) String courseId,
            @RequestParam(value="studentId", required=false) String studentId) {
        return enrollCourse(courseId, studentId);
    }

    public Status enrollCourse(String courseId, String studentId)
    {
        Status status = new Status();
        if(courseId == null || studentId == null)
        {
            status.setMissingParameter();
            return status;
        }
        /*
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        ICourseDao courseDao = ctx.getBean(ICourseDao.class);
        IStudentDao studentDao = ctx.getBean(IStudentDao.class);

        List<Course> courses = courseDao.findByCourseId(courseId);
        List<Student> students = studentDao.findByStudentId(studentId);
        */
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        //Course course = (Course)session.get(Course.class, courseId);
        //Student student = (Student)session.get(Student.class, studentId);
        Criteria cr = session.createCriteria(Course.class);
        cr.add(Restrictions.eq("courseId", courseId));
        List<Course> courses = cr.list();

        Criteria cr2 = session.createCriteria(Student.class);
        cr2.add(Restrictions.eq("studentId", studentId));
        List<Student> students = cr2.list();
        if(courses.isEmpty() || students.isEmpty())
        {
            status.setError();
            return status;
        }
        else
        {
            Course course = courses.get(0);
            Student student = students.get(0);
            course.getEnrolledStudents().add(student);
            student.getEnrolledCourses().add(course);
            session.save(course);
            session.save(student);
            session.getTransaction().commit();
            status.setSuccess();
            return status;
        }
    }
}
