package th.ac.kmitl.ce.ooad.cest.status;

/**
 * Created by atsaw on 14/10/2558.
 */
public class CreateStudentStatus {
    private String status;
    private String SUCCESS = "success";
    private String DUPLICATED_STUDENT_ID = "duplicatedStudentId";
    private String MISSING_PARAMERTER = "missingParameter";

    public String getStatus() {
        return status;
    }

    public void setSuccess() {
        this.status = SUCCESS;
    }

    public void setDuplicatedStudentId() {
        this.status = DUPLICATED_STUDENT_ID;
    }

    public void setMissingParameter() {
        this.status = MISSING_PARAMERTER;
    }
}
