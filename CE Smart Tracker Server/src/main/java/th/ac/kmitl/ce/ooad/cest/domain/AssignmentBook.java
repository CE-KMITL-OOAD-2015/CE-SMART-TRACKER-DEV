package th.ac.kmitl.ce.ooad.cest.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "assignment_book")
public class AssignmentBook
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private Student owner;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Assignment> assignments = new HashSet<>();

    // Need for hibernate
    public AssignmentBook()
    {

    }

    public AssignmentBook(Student owner)
    {
        this.owner = owner;
    }

    public void createAssignment(AssignmentDescription assignmentDescription)
    {
        Assignment assignment = new Assignment(assignmentDescription);
        assignments.add(assignment);
    }

    public void addAssignment(Assignment assignment)
    {
        this.assignments.add(assignment);
    }

    public boolean updateScoreByTitle(String title, double score)
    {

        for(Assignment assignment : assignments)
        {
            if(assignment.getAssignmentDescription().getTitle().equals(title))
            {
                assignment.setScore(score);
                return true;
            }
        }
        return false;
    }

    public Student getOwner()
    {
        return owner;
    }

    public void setOwner(Student owner)
    {
        this.owner = owner;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Set<Assignment> getAssignments()
    {
        return assignments;
    }

    public void setAssignments(Set<Assignment> assignments)
    {
        this.assignments = assignments;
    }
}
