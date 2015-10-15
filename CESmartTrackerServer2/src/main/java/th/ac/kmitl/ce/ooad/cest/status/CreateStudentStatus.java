package th.ac.kmitl.ce.ooad.cest.status;

public class CreateStudentStatus extends Status{
    private String DUPLICATED_STUDENT_ID = "duplicatedStudentId";

    public void setDuplicatedStudentId() {
        this.status = DUPLICATED_STUDENT_ID;
    }

}
