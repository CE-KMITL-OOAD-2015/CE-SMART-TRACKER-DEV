package th.ac.kmitl.ce.ooad.cest.service.response;

import th.ac.kmitl.ce.ooad.cest.domain.Course;

import java.util.List;
import java.util.Set;

public class ViewEnrolledCoursesResponse extends Status
{
    private Set<Course> enrolledCourses;

    public Set<Course> getEnrolledCourses()
    {
        return enrolledCourses;
    }

    public void setEnrolledCourses(Set<Course> enrolledCourses)
    {
        this.enrolledCourses = enrolledCourses;
    }

    public ViewEnrolledCoursesResponse()
    {
        super();
    }

    public ViewEnrolledCoursesResponse(StatusEnum statusEnum)
    {
        super(statusEnum);
    }
}
