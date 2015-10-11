package th.ac.kmitl.ce.ooad.cest.controller.course;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.config.AppConfig;
import th.ac.kmitl.ce.ooad.cest.dao.ICourseDao;
import th.ac.kmitl.ce.ooad.cest.entity.Course;

@Controller
@RequestMapping("/createCourse")
public class CreateCourseController {

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody
    Course request(
            @RequestParam(value="courseId", required=true) String courseId,
            @RequestParam(value="courseName", required=true) String courseName) {
        return saveCourse(courseId, courseName);
    }

    public Course saveCourse(String courseId, String courseName) {
        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName(courseName);
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        ICourseDao edao = ctx.getBean(ICourseDao.class);
        edao.saveCourse(course);
        return course;
    }

}