package th.ac.kmitl.ce.ooad.cest.controller.course;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.HibernateUtil;
import th.ac.kmitl.ce.ooad.cest.entity.Course;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/findCourse")
public class FindCourseController {

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody
    List<Course> request(
            @RequestParam(value="courseId", required=false) String courseId,
            @RequestParam(value="courseName", required=false) String courseName,
            @RequestParam(value="keyword", required=false) String keyword) {
        return findCourse(courseId, courseName, keyword);
    }

    public List<Course> findCourse(String courseId, String courseName, String keyword)
    {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Criteria cr = session.createCriteria(Course.class);
        if(keyword != null)
        {
            cr.add(Restrictions.or(
                            Restrictions.like("courseId", keyword, MatchMode.ANYWHERE), Restrictions.like("courseName", keyword, MatchMode.ANYWHERE)
            ));
        }
        else if(courseId != null)
        {
            cr.add(Restrictions.eq("courseId", courseId));
        }
        else if(courseName != null)
        {
            cr.add(Restrictions.eq("courseName", courseName));
        }
        cr.addOrder(Order.asc("courseName"));
        List<Course> courses = cr.list();
        return courses;
    }
}
