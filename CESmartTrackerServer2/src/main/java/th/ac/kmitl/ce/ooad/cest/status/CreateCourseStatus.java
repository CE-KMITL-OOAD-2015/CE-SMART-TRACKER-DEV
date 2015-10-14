package th.ac.kmitl.ce.ooad.cest.status;

/**
 * Created by atsaw on 14/10/2558.
 */
public class CreateCourseStatus {
    private String status;
    private String SUCCESS = "success";
    private String DUPLICATED_COURSE_ID = "duplicatedCourseId";
    private String MISSING_PARAMERTER = "missingParameter";

    public String getStatus() {
        return status;
    }

    public void setSuccess() {
        this.status = SUCCESS;
    }

    public void setDuplicatedCourseId() {
        this.status = DUPLICATED_COURSE_ID;
    }

    public void setMissingParameter() {
        this.status = MISSING_PARAMERTER;
    }
}
