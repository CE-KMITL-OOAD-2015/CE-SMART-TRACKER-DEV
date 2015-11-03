package th.ac.kmitl.ce.ooad.cest.domain;

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
    private float maxScore;

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
        if(this.title.equals(assignmentDescription2.getTitle()))
        {
            return true;
        }
        else
        {
            return false;
        }
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

    public float getMaxScore()
    {
        return maxScore;
    }

    public void setMaxScore(float maxScore)
    {
        this.maxScore = maxScore;
    }
}
