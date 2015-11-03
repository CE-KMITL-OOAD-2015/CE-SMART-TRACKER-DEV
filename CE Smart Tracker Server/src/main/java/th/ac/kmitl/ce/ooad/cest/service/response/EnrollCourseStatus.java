package th.ac.kmitl.ce.ooad.cest.service.response;


public class EnrollCourseStatus extends Status{
    public EnrollCourseStatus(EnrollCourseStatusEnum enrollCourseStatusEnum)
    {
        super();
        switch(enrollCourseStatusEnum)
        {
            case COURSE_NOT_FOUND:
                this.status = "courseNotFound";
                break;
            default:
                this.status = "error";
        }
    }

    public EnrollCourseStatus(StatusEnum statusEnum)
    {
        super(statusEnum);
    }
}
