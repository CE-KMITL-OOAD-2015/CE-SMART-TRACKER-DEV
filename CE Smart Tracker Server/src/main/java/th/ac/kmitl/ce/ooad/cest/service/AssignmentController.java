package th.ac.kmitl.ce.ooad.cest.service;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import th.ac.kmitl.ce.ooad.cest.domain.*;
import th.ac.kmitl.ce.ooad.cest.repository.CourseRepository;
import th.ac.kmitl.ce.ooad.cest.repository.StudentRepository;
import th.ac.kmitl.ce.ooad.cest.repository.TeacherRepository;
import th.ac.kmitl.ce.ooad.cest.service.response.Response;
import th.ac.kmitl.ce.ooad.cest.service.response.ResponseEnum;

import java.util.Set;

@Controller
public class AssignmentController
{
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    @RequestMapping("/addAssignment")
    @ResponseBody
    public Response requestAddAssignment(@RequestParam(value = "sessionId", required = true) String sessionId,
                                         @RequestParam(value = "courseId", required = true) String courseId,
                                         @RequestParam(value = "title", required = true) String title,
                                         @RequestParam(value = "description", required = true) String description,
                                         @RequestParam(value = "maxScore", required = true) double maxScore,
                                         @RequestParam(value = "dueDate", required = true) String dueDate)
    {
        return addAssignment(sessionId.trim(), courseId.trim(), title.trim(), description.trim(), maxScore, dueDate);
    }


    private Response addAssignment(String sessionId, String courseId, String title, String description, double maxScore, String dueDate)
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        DateTime dateTime = formatter.parseDateTime(dueDate);

        Teacher teacher = teacherRepository.findFirstBySessionId(sessionId);
        if (teacher == null)
        {
            return new Response(ResponseEnum.INVALID_SESSION);
        }

        Course course = courseRepository.findFirstByCourseId(courseId);
        if (course == null)
        {
            return new Response(ResponseEnum.COURSE_NOT_FOUND);
        }
        else if (!course.isTeaching(teacher))
        {
            return new Response(ResponseEnum.NOT_TEACHING);
        }
        else if (course.addAssignment(title, description, maxScore, dateTime))
        {
            courseRepository.save(course);
            return new Response(ResponseEnum.SUCCESS);
        }
        else
        {
            return new Response(ResponseEnum.DUPLICATED_TITLE);
        }
    }

    @RequestMapping("/viewAllAssignmentDescriptions")
    @ResponseBody
    public ViewAllAssignmentDescriptionResponse requestViewAllAssignmentDescriptions(@RequestParam(value = "courseId", required = true) String courseId)
    {
        return viewAllAssignmentDescriptions(courseId.trim());
    }

    private ViewAllAssignmentDescriptionResponse viewAllAssignmentDescriptions(String courseId)
    {
        Course course = courseRepository.findFirstByCourseId(courseId);
        if (course == null)
        {
            return new ViewAllAssignmentDescriptionResponse(ResponseEnum.COURSE_NOT_FOUND);
        }

        return new ViewAllAssignmentDescriptionResponse(ResponseEnum.SUCCESS, course.getAssignmentDescriptions());
    }

    @RequestMapping("/updateAssignmentScore")
    @ResponseBody
    public Response requestUpdateAssignmentScore(@RequestParam(value = "sessionId", required = true) String sessionId,
                                                 @RequestParam(value = "courseId", required = true) String courseId,
                                                 @RequestParam(value = "username", required = true) String username,
                                                 @RequestParam(value = "title", required = true) String title,
                                                 @RequestParam(value = "score", required = true) double score)
    {
        return updateAssignmentScore(sessionId.trim(), courseId.trim(), username.trim(), title.trim(), score);
    }

    private Response updateAssignmentScore(String sessionId, String courseId, String username, String title, double score)
    {
        Teacher teacher = teacherRepository.findFirstBySessionId(sessionId);
        if (teacher == null)
        {
            return new Response(ResponseEnum.INVALID_SESSION);
        }

        Course course = courseRepository.findFirstByCourseId(courseId);
        if (course == null)
        {
            return new Response(ResponseEnum.COURSE_NOT_FOUND);
        }

        if (!course.isTeaching(teacher))
        {
            return new Response(ResponseEnum.NOT_TEACHING);
        }


        Student student = studentRepository.findFirstByUsername(username);
        if (student == null)
        {
            return new Response(ResponseEnum.USERNAME_NOT_FOUND);
        }

        if (course.updateAssignmentScore(student, title, score))
        {
            courseRepository.save(course);
            return new Response(ResponseEnum.SUCCESS);
        }
        else
        {
            return new Response(ResponseEnum.CAN_NOT_UPDATE);
        }
    }

    @RequestMapping("/viewSelfAssignmentBook")
    @ResponseBody
    public ViewAssignmentBookResponse requestViewSelfAssignmentBook(@RequestParam(value = "sessionId", required = true) String sessionId,
                                                                @RequestParam(value = "courseId", required = true) String courseId)
    {
        return viewSelfAssignmentBook(sessionId.trim(), courseId.trim());
    }

    private ViewAssignmentBookResponse viewSelfAssignmentBook(String sessionId, String courseId)
    {
        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course == null)
        {
            return new ViewAssignmentBookResponse(ResponseEnum.COURSE_NOT_FOUND);
        }

        Student student = studentRepository.findFirstBySessionId(sessionId);
        if(student != null)
        {
            if(course.isEnrolled(student))
            {
                return new ViewAssignmentBookResponse(ResponseEnum.SUCCESS, course.getAssignmentBookByStudent(student));
            }
            else
            {
                return new ViewAssignmentBookResponse(ResponseEnum.NOT_ENROLLED);
            }
        }
        else
        {
            return new ViewAssignmentBookResponse(ResponseEnum.INVALID_SESSION);
        }
    }

    @RequestMapping("/viewAllAssignmentBook")
    @ResponseBody
    public ViewAllAssignmentBookResponse requestViewAllAssignmentBook(@RequestParam(value = "sessionId", required = true) String sessionId,
                                                                @RequestParam(value = "courseId", required = true) String courseId)
    {
        return viewAllAssignmentBook(sessionId.trim(), courseId.trim());
    }

    private ViewAllAssignmentBookResponse viewAllAssignmentBook(String sessionId, String courseId)
    {
        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course == null)
        {
            return new ViewAllAssignmentBookResponse(ResponseEnum.COURSE_NOT_FOUND);
        }

        Teacher teacher = teacherRepository.findFirstBySessionId(sessionId);
        if(teacher != null)
        {
            if(course.isTeaching(teacher))
            {
                return new ViewAllAssignmentBookResponse(ResponseEnum.SUCCESS, course.getAssignmentBooks());
            }
            else
            {
                return new ViewAllAssignmentBookResponse(ResponseEnum.NOT_TEACHING);
            }
        }
        else
        {
            return new ViewAllAssignmentBookResponse(ResponseEnum.INVALID_SESSION);
        }
    }

    @RequestMapping("/viewAssignmentBookOfStudent")
    @ResponseBody
    public ViewAssignmentBookResponse requestViewAssignmentBookOfStudent(@RequestParam(value = "sessionId", required = true) String sessionId,
                                                                @RequestParam(value = "courseId", required = true) String courseId,
                                                                @RequestParam(value = "username", required = true) String username)
    {
        return viewAssignmentBookOfStudent(sessionId.trim(), courseId.trim(), username.trim());
    }

    private ViewAssignmentBookResponse viewAssignmentBookOfStudent(String sessionId, String courseId,  String username)
    {
        Course course = courseRepository.findFirstByCourseId(courseId);
        if(course == null)
        {
            return new ViewAssignmentBookResponse(ResponseEnum.COURSE_NOT_FOUND);
        }

        Teacher teacher = teacherRepository.findFirstBySessionId(sessionId);
        if(teacher == null)
        {
            return new ViewAssignmentBookResponse(ResponseEnum.INVALID_SESSION);
        }
        else if(!course.isTeaching(teacher))
        {
            return new ViewAssignmentBookResponse(ResponseEnum.NOT_TEACHING);
        }

        Student student = studentRepository.findFirstByUsername(username);
        if(student == null)
        {
            return new ViewAssignmentBookResponse(ResponseEnum.USERNAME_NOT_FOUND);
        }
        else if(!course.isEnrolled(student))
        {
            return new ViewAssignmentBookResponse(ResponseEnum.NOT_ENROLLED);
        }

        // everything is fine here
        return new ViewAssignmentBookResponse(ResponseEnum.SUCCESS, course.getAssignmentBookByStudent(student));
    }

    private class ViewAllAssignmentBookResponse extends Response
    {
        private Set<AssignmentBook> assignmentBooks;

        public ViewAllAssignmentBookResponse(ResponseEnum responseEnum, Set<AssignmentBook> assignmentBooks)
        {
            super(responseEnum);
            this.assignmentBooks = assignmentBooks;
        }

        public ViewAllAssignmentBookResponse(ResponseEnum responseEnum)
        {
            super(responseEnum);
        }

        public Set<AssignmentBook> getAssignmentBooks()
        {
            return assignmentBooks;
        }
    }

    private class ViewAssignmentBookResponse extends Response
    {
        private AssignmentBook assignmentBook;

        public ViewAssignmentBookResponse(ResponseEnum responseEnum, AssignmentBook assignmentBook)
        {
            super(responseEnum);
            this.assignmentBook = assignmentBook;
        }

        public ViewAssignmentBookResponse(ResponseEnum responseEnum)
        {
            super(responseEnum);
            this.assignmentBook = null;
        }

        public AssignmentBook getAssignmentBook()
        {
            return assignmentBook;
        }
    }

    private class ViewAllAssignmentDescriptionResponse extends Response
    {
        private Set<AssignmentDescription> assignmentDescriptions;

        public Set<AssignmentDescription> getAssignmentDescriptions()
        {
            return assignmentDescriptions;
        }

        public ViewAllAssignmentDescriptionResponse(ResponseEnum responseEnum)
        {
            super(responseEnum);
            this.assignmentDescriptions = null;
        }

        public ViewAllAssignmentDescriptionResponse(ResponseEnum responseEnum, Set<AssignmentDescription> assignmentDescriptions)
        {
            super(responseEnum);
            this.assignmentDescriptions = assignmentDescriptions;
        }
    }
}
