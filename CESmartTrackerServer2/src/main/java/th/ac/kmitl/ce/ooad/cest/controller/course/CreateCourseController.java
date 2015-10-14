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
import th.ac.kmitl.ce.ooad.cest.status.CreateCourseStatus;

import java.util.List;

@Controller
@RequestMapping("/createCourse")
public class CreateCourseController {

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody CreateCourseStatus request(
            @RequestParam(value="courseId", required=false) String courseId,
            @RequestParam(value="courseName", required=false) String courseName) {
        return saveCourse(courseId, courseName);
    }

    public CreateCourseStatus saveCourse(String courseId, String courseName) {
        CreateCourseStatus status = new CreateCourseStatus();
        if(courseId == null || courseName == null)
        {
            status.setMissingParameter();
            return status;
        }

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        ICourseDao edao = ctx.getBean(ICourseDao.class);

        List<Course> courses = edao.findByCourseId(courseId);
        if(courses.isEmpty())
        {
            Course course = new Course();
            course.setCourseId(courseId);
            course.setCourseName(courseName);
            edao.saveCourse(course);

            status.setSuccess();
            return status;
        }
        else
        {
            status.setDuplicatedCourseId();
            return status;
        }
    }
}