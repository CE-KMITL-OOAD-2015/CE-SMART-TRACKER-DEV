package th.ac.kmitl.ce.ooad.cest.service.response;

public class LoginStatus extends Status {

    private String sessionId;

    public LoginStatus(LoginStatusEnum loginStatusEnum)
    {
        super();
        switch(loginStatusEnum)
        {
            case USERNAME_NOT_FOUND:
                this.status = "usernameNotFound";
                break;
            case INVALID_PASSWORD:
                this.status = "invalidPassword";
                break;
            default:
                this.status = "error";
        }
    }

    public LoginStatus(StatusEnum statusEnum)
    {
        super(statusEnum);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
