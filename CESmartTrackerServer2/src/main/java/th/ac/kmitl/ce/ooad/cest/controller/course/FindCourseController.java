package th.ac.kmitl.ce.ooad.cest.controller.course;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import th.ac.kmitl.ce.ooad.cest.entity.Course;
import th.ac.kmitl.ce.ooad.cest.entity.Student;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/findCourse")
public class FindCourseController {

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody
    List<Course> request(
            @RequestParam(value="courseId", required=false) String courseId,
            @RequestParam(value="courseName", required=false) String courseName) {
        return findCourse2(courseId, courseName);
    }

    public List<Course> findCourse2(String courseId, String courseName)
    {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Course.class);
        if(courseId != null)
        {
            cr.add(Restrictions.eq("courseId", courseId));
            List<Course> courses = cr.list();
            return courses;
        }
        else if(courseName != null)
        {
            cr.add(Restrictions.eq("courseName", courseName));
            List<Course> courses = cr.list();
            return courses;
        }
        else
        {
            List<Course> courses = cr.list();
            return courses;
        }
    }

    public List<Course> findCourse(String courseId, String courseName)
    {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        ICourseDao courseDao = ctx.getBean(ICourseDao.class);
        List<Course> courses = null;
        if(courseId != null)
        {
            courses = courseDao.findByCourseId(courseId);
        }
        else if(courseName != null)
        {
            courses = courseDao.findByCourseName(courseName);
        }
        else
        {
            courses = courseDao.findAllCourse();
        }
        return courses;
    }

}
