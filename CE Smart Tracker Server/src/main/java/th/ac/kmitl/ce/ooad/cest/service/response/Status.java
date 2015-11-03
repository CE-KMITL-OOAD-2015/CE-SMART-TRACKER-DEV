package th.ac.kmitl.ce.ooad.cest.service.response;

public class Status {
    protected String status;
    private String SUCCESS = "success";
    private String MISSING_PARAMERTER = "missingParameter";
    private String ERROR = "error";

    public Status()
    {
        this.status = "error";
    }

    public Status(StatusEnum statusEnum)
    {
        switch(statusEnum)
        {
            case SUCCESS:
                this.status = "success";
                break;
            case MISSING_PARAMETER:
                this.status = "missingParameter";
                break;
            case INVALID_SESSION:
                this.status = "invalidSession";
                break;
            case ERROR:
            default:
                this.status = "error";
        }
    }

    public String getStatus() {
        return status;
    }
}