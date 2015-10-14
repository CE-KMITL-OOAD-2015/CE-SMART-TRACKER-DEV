package th.ac.kmitl.ce.ooad.cest.status;

public class CreateCourseStatus extends Status{
    private String DUPLICATED_COURSE_ID = "duplicatedCourseId";

    public void setDuplicatedCourseId() {
        this.status = DUPLICATED_COURSE_ID;
    }

}
