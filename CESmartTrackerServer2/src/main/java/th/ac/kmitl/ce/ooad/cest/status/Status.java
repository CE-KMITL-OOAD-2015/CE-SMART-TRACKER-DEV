package th.ac.kmitl.ce.ooad.cest.status;

public class Status {
    protected String status;
    private String SUCCESS = "success";
    private String MISSING_PARAMERTER = "missingParameter";
    private String ERROR = "error";

    public String getStatus() {
        return status;
    }

    public void setSuccess() {
        this.status = SUCCESS;
    }

    public void setMissingParameter() {
        this.status = MISSING_PARAMERTER;
    }

    public void setError() {
        this.status = ERROR;
    }
}
