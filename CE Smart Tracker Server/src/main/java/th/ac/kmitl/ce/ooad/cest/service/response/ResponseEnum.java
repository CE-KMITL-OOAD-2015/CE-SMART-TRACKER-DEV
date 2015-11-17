package th.ac.kmitl.ce.ooad.cest.service.response;

public enum ResponseEnum
{
    SUCCESS("success"),
    ERROR("error"),
    //MISSING_PARAMETER("missingParameter"),
    INVALID_SESSION("invalidSession"),
    COURSE_NOT_FOUND("courseNotFound"),
    DUPLICATED_TITLE("duplicatedTitle"),
    DUPLICATED_COURSE_ID("duplicatedCourseId"),
    DUPLICATED_COURSE_NAME("duplicatedCourseName"),
    NOT_TEACHING("notTeaching"),
    DUPLICATED_USERNAME("duplicatedUsername"),
    DUPLICATED_STUDENT_ID("duplicatedStudentId"),
    NOT_ENROLLED("notEnrolled"),
    ALREADY_ENROLLED("alreadyEnrolled"),
    USERNAME_NOT_FOUND("usernameNotFound"),
    INVALID_PASSWORD("invalidPassword"),
    //ALREADY_RATED("alreadyRated"),
    ALREADY_COMMENTED("alreadyCommented"),
    CAN_NOT_UPDATE("canNotUpdate"),
    DUPLICATED_FACEBOOK_ID("duplicatedFacebookId"),
    FACEBOOK_ID_NOT_FOUND("facebookIdNotFound");

    private String message;

    ResponseEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}