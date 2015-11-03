package th.ac.kmitl.ce.ooad.cest.service.response;

public class ViewRatingResponse extends Status
{
    private double averageRating;

    public ViewRatingResponse(ViewRatingResponseEnum viewRatingResponseEnum)
    {
        super();
        switch(viewRatingResponseEnum)
        {
            case COURSE_NOT_FOUND:
                this.status = "courseNotFound";
                break;
            default:
                this.status = "error";
        }
    }

    public ViewRatingResponse(StatusEnum statusEnum)
    {
        super(statusEnum);
    }

    public double getAverageRating()
    {
        return averageRating;
    }

    public void setAverageRating(double averageRating)
    {
        this.averageRating = averageRating;
    }
}
