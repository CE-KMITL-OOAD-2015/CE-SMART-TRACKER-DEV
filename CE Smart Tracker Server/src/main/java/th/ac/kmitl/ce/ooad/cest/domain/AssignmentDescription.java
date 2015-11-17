package th.ac.kmitl.ce.ooad.cest.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;

@Entity
@Table(name = "assignment_description")
public class AssignmentDescription
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String description;
    private double maxScore;
    private DateTime dueDate;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof AssignmentDescription))
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }

        AssignmentDescription assignmentDescription2 = (AssignmentDescription) obj;
        return this.title.equals(assignmentDescription2.getTitle());
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getMaxScore()
    {
        return maxScore;
    }

    public void setMaxScore(double maxScore)
    {
        this.maxScore = maxScore;
    }

    public String getDueDate()
    {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
        return fmt.print(dueDate);
    }

    public void setDueDate(DateTime dueDate)
    {
        this.dueDate = dueDate;
    }
}
