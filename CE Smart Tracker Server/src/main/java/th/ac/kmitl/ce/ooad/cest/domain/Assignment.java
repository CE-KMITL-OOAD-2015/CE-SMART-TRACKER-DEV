package th.ac.kmitl.ce.ooad.cest.domain;

import javax.persistence.*;

@Entity
@Table(name = "assignment")
public class Assignment
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private double score;
    @ManyToOne(cascade = CascadeType.ALL)
    private AssignmentDescription assignmentDescription;

    // Need for hibernate
    public Assignment()
    {

    }

    public Assignment(AssignmentDescription assignmentDescription)
    {
        this.assignmentDescription = assignmentDescription;
        this.score = 0;
    }

    public double getScore()
    {
        return score;
    }

    public void setScore(double score)
    {
        if(score < 0)
        {
            this.score = 0;
        }
        else if(score > assignmentDescription.getMaxScore())
        {
            this.score = assignmentDescription.getMaxScore();
        }
        else
        {
            this.score = score;
        }
    }

    public AssignmentDescription getAssignmentDescription()
    {
        return assignmentDescription;
    }

    public void setAssignmentDescription(AssignmentDescription assignmentDescription)
    {
        this.assignmentDescription = assignmentDescription;
    }
}
