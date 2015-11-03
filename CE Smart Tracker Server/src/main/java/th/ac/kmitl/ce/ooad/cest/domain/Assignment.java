package th.ac.kmitl.ce.ooad.cest.domain;

import javax.persistence.*;

@Entity
@Table(name = "assignment")
public class Assignment
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private float score;
    @ManyToOne(cascade = CascadeType.ALL)
    private AssignmentDescription assignmentDescription = new AssignmentDescription();

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public float getScore()
    {
        return score;
    }

    public void setScore(float score)
    {
        this.score = score;
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
