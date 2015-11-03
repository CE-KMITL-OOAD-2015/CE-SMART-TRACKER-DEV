package th.ac.kmitl.ce.ooad.cest.service.response;

public class CreateStudentStatus extends Status{

    public CreateStudentStatus(CreateStudentStatusEnum createStudentStatusEnum)
    {
        super();
        switch(createStudentStatusEnum)
        {
            case DUPLICATED_USERNAME:
                this.status = "duplicatedUsername";
                break;
            case DUPLICATED_STUDENT_ID:
                this.status = "duplicatedStudentId";
                break;
            default:
                this.status = "error";
        }
    }

    public CreateStudentStatus(StatusEnum statusEnum)
    {
        super(statusEnum);
    }
}
