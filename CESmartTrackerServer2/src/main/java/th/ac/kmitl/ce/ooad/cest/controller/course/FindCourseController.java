package th.ac.kmitl.ce.ooad.cest.controller.course;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.config.AppConfig;
import th.ac.kmitl.ce.ooad.cest.dao.course.ICourseDao;
import th.ac.kmitl.ce.ooad.cest.entity.Course;

import java.util.List;

@Controller
@RequestMapping("/findCourse")
public class FindCourseController {

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody
    List<Course> request(
            @RequestParam(value="courseId", required=false) String courseId,
            @RequestParam(value="courseName", required=false) String courseName) {
        return findCourse(courseId, courseName);
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
