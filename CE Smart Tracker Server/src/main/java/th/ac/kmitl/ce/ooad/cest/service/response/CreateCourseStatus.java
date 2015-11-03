package th.ac.kmitl.ce.ooad.cest.service.response;

public class CreateCourseStatus extends Status{
    public CreateCourseStatus(CreateCourseStatusEnum createCourseStatusEnum)
    {
        super();
        switch(createCourseStatusEnum)
        {
            case DUPLICATED_COURSE_ID:
                this.status = "duplicatedCourseId";
                break;
            default:
                this.status = "error";
        }
    }

    public CreateCourseStatus(StatusEnum statusEnum)
    {
        super(statusEnum);
    }
}
