package th.ac.kmitl.ce.ooad.cest.service.response;

public class Response {
    private ResponseEnum status;

    public Response()
    {
        this.status = ResponseEnum.ERROR;
    }
    public Response(ResponseEnum responseEnum)
    {
        this.status = responseEnum;
    }

    public String getStatus() {
        return status.getMessage();
    }
}
