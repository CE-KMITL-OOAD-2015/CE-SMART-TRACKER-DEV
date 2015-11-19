package th.ac.kmitl.ce.ooad.cest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.domain.Comment;
import th.ac.kmitl.ce.ooad.cest.domain.Course;
import th.ac.kmitl.ce.ooad.cest.domain.Student;
import th.ac.kmitl.ce.ooad.cest.repository.CourseRepository;
import th.ac.kmitl.ce.ooad.cest.repository.StudentRepository;
import th.ac.kmitl.ce.ooad.cest.service.response.Response;
import th.ac.kmitl.ce.ooad.cest.service.response.ResponseEnum;

import java.util.List;

@Controller
public class RateCommentController
{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @RequestMapping("/viewRating")
    @ResponseBody
    public ViewRatingResponse requestViewRating(@RequestParam(value="courseId", required=true) String courseId)
    {
        return viewRating(courseId.trim());
    }

    private ViewRatingResponse viewRating(String courseId)
    {
        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course == null)
        {
            return new ViewRatingResponse(ResponseEnum.COURSE_NOT_FOUND);
        }
        else
        {
            ViewRatingResponse viewRatingResponse = new ViewRatingResponse(ResponseEnum.SUCCESS);
            viewRatingResponse.setAverageRating(course.getAverageRating());
            return viewRatingResponse;
        }
    }

    @RequestMapping("/viewComment")
    @ResponseBody
    public ViewCommentResponse requestViewComment(@RequestParam(value="courseId", required=true) String courseId)
    {
        return viewComment(courseId.trim());
    }

    private ViewCommentResponse viewComment(String courseId)
    {
        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course == null)
        {
            return new ViewCommentResponse(ResponseEnum.COURSE_NOT_FOUND);
        }
        else
        {
            ViewCommentResponse viewRatingResponse = new ViewCommentResponse(ResponseEnum.SUCCESS);
            viewRatingResponse.setComments(course.getComments());
            return viewRatingResponse;
        }
    }
    @RequestMapping("/commentCourse")
    @ResponseBody
    public Response requestRateCourse(@RequestParam(value="sessionId", required=true) String sessionId,
                                                 @RequestParam(value="courseId", required=true) String courseId,
                                                 @RequestParam(value="message", required=true) String message)
    {
        return commentCourse(sessionId.trim(), courseId.trim(), message.trim());

    }

    private Response commentCourse(String sessionId, String courseId, String message)
    {
        Student student = studentRepository.findFirstBySessionId(sessionId);
        if(student == null)
        {
            return new Response(ResponseEnum.INVALID_SESSION);
        }

        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course == null)
        {
            return new Response(ResponseEnum.COURSE_NOT_FOUND);
        }
        else if(!course.isEnrolledBy(student))
        {
            return new Response(ResponseEnum.NOT_ENROLLED);
        }
        else if(course.isCommented(student))
        {
            return new Response(ResponseEnum.ALREADY_COMMENTED);
        }
        else
        {
            course.addComment(student, message);
            courseRepository.save(course);
            return new Response(ResponseEnum.SUCCESS);
        }
    }

    @RequestMapping("/rateCourse")
    @ResponseBody
    public Response requestRateCourse(@RequestParam(value="sessionId", required=true) String sessionId,
                                                 @RequestParam(value="courseId", required=true) String courseId,
                                                 @RequestParam(value="point", required=true) int point)
    {
        return rateCourse(sessionId.trim(), courseId.trim(), point);

    }

    private Response rateCourse(String sessionId, String courseId, int point)
    {
        Student student = studentRepository.findFirstBySessionId(sessionId);
        if(student == null)
        {
            return new Response(ResponseEnum.INVALID_SESSION);
        }

        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course == null)
        {
            return new Response(ResponseEnum.COURSE_NOT_FOUND);
        }
        else if(!course.isEnrolledBy(student))
        {
            return new Response(ResponseEnum.NOT_ENROLLED);
        }
        else
        {
            course.addRating(student, point);
            courseRepository.save(course);
            return new Response(ResponseEnum.SUCCESS);
        }
    }

    private class ViewRatingResponse extends Response
    {
        private double averageRating;

        public ViewRatingResponse(ResponseEnum responseEnum)
        {
            super(responseEnum);
        }

        public double getAverageRating()
        {
            return averageRating;
        }

        public void setAverageRating(double averageRating)
        {
            this.averageRating = averageRating;
        }
    }

    private class ViewCommentResponse extends Response
    {
        private List<Comment> comments;

        public ViewCommentResponse(ResponseEnum responseEnum)
        {
            super(responseEnum);
        }

        public List<Comment> getComments()
        {
            return comments;
        }

        public void setComments(List<Comment> comments)
        {
            this.comments = comments;
        }
    }
}
