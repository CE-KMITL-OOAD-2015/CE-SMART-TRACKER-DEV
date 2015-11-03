package th.ac.kmitl.ce.ooad.cest.service.response;

public class RateCourseResponse extends Status
{
    public RateCourseResponse(RateCourseResponseEnum rateCourseResponseEnum)
    {
        super();
        switch(rateCourseResponseEnum)
        {
            case COURSE_NOT_FOUND:
                this.status = "courseNotFound";
                break;
            case ALREADY_RATED:
                this.status = "alreadyRated";
                break;
            case NOT_ENROLLED:
                this.status = "notEnrolled";
                break;
            default:
                this.status = "error";
        }
    }

    public RateCourseResponse(StatusEnum statusEnum)
    {
        super(statusEnum);
    }
}
