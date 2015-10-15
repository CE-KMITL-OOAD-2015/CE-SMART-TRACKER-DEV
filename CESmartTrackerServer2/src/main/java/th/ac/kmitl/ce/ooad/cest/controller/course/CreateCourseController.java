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
import th.ac.kmitl.ce.ooad.cest.status.CreateCourseStatus;

import java.util.List;

@Controller
@RequestMapping("/createCourse")
public class CreateCourseController {

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody CreateCourseStatus request(
            @RequestParam(value="courseId", required=false) String courseId,
            @RequestParam(value="courseName", required=false) String courseName,
            @RequestParam(value="faculty", required=false) String faculty,
            @RequestParam(value="department", required=false) String department,
            @RequestParam(value="description", required=false) String description,
            @RequestParam(value="courseDay", required=false) String courseDay,
            @RequestParam(value="courseTime", required=false) String courseTime) {
        return saveCourse(courseId, courseName, faculty, department, description, courseDay, courseTime);
    }

    public CreateCourseStatus saveCourse(String courseId, String courseName, String faculty, String department, String description, String courseDay, String courseTime) {
        CreateCourseStatus status = new CreateCourseStatus();
        if(courseId == null || courseName == null || faculty == null)
        {
            status.setMissingParameter();
            return status;
        }
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();

        Criteria cr = session.createCriteria(Course.class);
        cr.add(Restrictions.eq("courseId", courseId));
        List<Course> courses = cr.list();
        if(!courses.isEmpty())
        {
            status.setDuplicatedCourseId();
            return status;
        }
        else
        {
            Course newCourse = new Course();
            newCourse.setCourseId(courseId);
            newCourse.setCourseName(courseName);
            newCourse.setDescription(description);
            newCourse.setFaculty(faculty);
            newCourse.setDepartment(department);
            newCourse.setCourseDay(courseDay);
            newCourse.setCourseTime(courseTime);
            session.save(newCourse);
            session.getTransaction().commit();
            status.setSuccess();
            return status;
        }
    }
}