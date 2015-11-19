package th.ac.kmitl.ce.ooad.cest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.domain.Announcement;
import th.ac.kmitl.ce.ooad.cest.domain.Course;
import th.ac.kmitl.ce.ooad.cest.domain.Student;
import th.ac.kmitl.ce.ooad.cest.domain.Teacher;
import th.ac.kmitl.ce.ooad.cest.repository.CourseRepository;
import th.ac.kmitl.ce.ooad.cest.repository.StudentRepository;
import th.ac.kmitl.ce.ooad.cest.repository.TeacherRepository;
import th.ac.kmitl.ce.ooad.cest.repository.UserRepository;
import th.ac.kmitl.ce.ooad.cest.service.response.Response;
import th.ac.kmitl.ce.ooad.cest.service.response.ResponseEnum;

import java.util.List;

@Controller
public class AnnouncementController
{
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    /*@Autowired
    private Validator validator;*/

    @RequestMapping("/addAnnouncement")
    @ResponseBody
    public Response requestAddAnnouncement(@RequestParam(value="sessionId", required=true) String sessionId,
                                           @RequestParam(value="courseId", required=true) String courseId,
                                           @RequestParam(value="title", required=true) String title,
                                           @RequestParam(value="description", required=true) String description)
    {
        return addAnnouncement(sessionId, courseId, title, description);
    }

    private Response addAnnouncement(String sessionId, String courseId, String title, String description)
    {
        Teacher teacher = teacherRepository.findFirstBySessionId(sessionId);
        if(teacher != null)
        {
            Course course = courseRepository.findFirstByCourseId(courseId);
            if(course != null)
            {
                if(course.isTeachingBy(teacher))
                {
                    course.addAnnouncement(teacher, title, description);
                    courseRepository.save(course);
                    return new Response(ResponseEnum.SUCCESS);
                }
                else
                {
                    return new Response(ResponseEnum.NOT_TEACHING);
                }
            }
            else
            {
                return new Response(ResponseEnum.COURSE_NOT_FOUND);
            }
        }
        else
        {
            return new Response(ResponseEnum.INVALID_SESSION);
        }
    }

    @RequestMapping("/viewAnnouncement")
    @ResponseBody
    public ViewAnnouncementResponse requestViewAnnouncement(@RequestParam(value="sessionId", required=true) String sessionId,
                                                            @RequestParam(value="courseId", required=true) String courseId)
    {
        return viewAnnouncement(sessionId, courseId);
    }

    private ViewAnnouncementResponse viewAnnouncement(String sessionId, String courseId)
    {
        Course course = courseRepository.findFirstByCourseId(courseId);
        Student student = studentRepository.findFirstBySessionId(sessionId);
        Teacher teacher = teacherRepository.findFirstBySessionId(sessionId);
        if(student == null && teacher == null)
        {
            return new ViewAnnouncementResponse(ResponseEnum.INVALID_SESSION);
        }
        else if(course == null)
        {
            return new ViewAnnouncementResponse(ResponseEnum.COURSE_NOT_FOUND);
        }
        else if(student != null && !course.isEnrolledBy(student))
        {
            return new ViewAnnouncementResponse(ResponseEnum.NOT_ENROLLED);
        }
        else if(teacher != null && !course.isTeachingBy(teacher))
        {
            return new ViewAnnouncementResponse(ResponseEnum.NOT_TEACHING);
        }
        else
        {
            return new ViewAnnouncementResponse(ResponseEnum.SUCCESS, course.getAnnouncements());
        }
    }

    private class ViewAnnouncementResponse extends Response
    {
        private List<Announcement> announcements;

        public ViewAnnouncementResponse(ResponseEnum responseEnum)
        {
            super(responseEnum);
            announcements = null;
        }

        public ViewAnnouncementResponse(ResponseEnum responseEnum, List<Announcement> announcements)
        {
            super(responseEnum);
            this.announcements = announcements;
        }

        public List<Announcement> getAnnouncements()
        {
            return announcements;
        }
    }
}
