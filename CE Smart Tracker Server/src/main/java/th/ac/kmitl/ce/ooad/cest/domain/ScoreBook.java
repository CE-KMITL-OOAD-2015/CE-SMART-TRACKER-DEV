package th.ac.kmitl.ce.ooad.cest.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ScoreBook implements Comparable<ScoreBook>
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private Student owner;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<AssignmentScore> assignments = new HashSet<>();

    // Need for hibernate
    public ScoreBook()
    {

    }

    public ScoreBook(Student owner)
    {
        this.owner = owner;
    }

    public void createAssignmentScore(AssignmentDescription assignmentDescription)
    {
        AssignmentScore assignmentScore = new AssignmentScore(assignmentDescription);
        assignments.add(assignmentScore);
    }

    public boolean updateScoreByTitle(String title, double score)
    {

        for(AssignmentScore assignmentScore : assignments)
        {
            if(assignmentScore.getAssignmentDescription().getTitle().equals(title))
            {
                assignmentScore.setScore(score);
                return true;
            }
        }
        return false;
    }

    public double getSumScore()
    {
        double sum = 0;
        for(AssignmentScore assignmentScore : assignments)
        {
            sum += assignmentScore.getScore();
        }
        return sum;
    }

    public double getMaxSumScore()
    {
        double maxSum = 0;
        for(AssignmentScore assignmentScore : assignments)
        {
            maxSum += assignmentScore.getAssignmentDescription().getMaxScore();
        }
        return maxSum;
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

    public Set<AssignmentScore> getAssignments()
    {
        return assignments;
    }

    public void setAssignments(Set<AssignmentScore> assignmentScores)
    {
        this.assignments = assignmentScores;
    }

    @Override
    public int compareTo(ScoreBook scoreBook)
    {
        return (this.getOwner().getStudentId() - scoreBook.getOwner().getStudentId());
    }
}
