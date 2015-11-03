package th.ac.kmitl.ce.ooad.cest.service.response;

public class AddAssignmentResponse extends Status
{
    public AddAssignmentResponse(AddAssignmentResponseEnum addAssignmentResponseEnum)
    {
        super();
        switch(addAssignmentResponseEnum)
        {
            case COURSE_NOT_FOUND:
                this.status = "courseNotFound";
                break;
            case DUPLICATED_TITLE:
                this.status = "duplicatedTitle";
                break;
            case NOT_TEACHING:
                this.status = "notTeaching";
                break;
            default:
                this.status = "error";
        }
    }

    public AddAssignmentResponse(StatusEnum statusEnum)
    {
        super(statusEnum);
    }
}
