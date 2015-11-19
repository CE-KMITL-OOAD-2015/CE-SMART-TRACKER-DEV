package th.ac.kmitl.ce.ooad.cest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Assignment
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<ScoreBook> scoreBooks = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<AssignmentDescription> assignmentDescriptions = new HashSet<>();

    public ScoreBook getAssignmentBookByStudent(Student student)
    {
        if(student == null)
        {
            return null;
        }

        for(ScoreBook scoreBook : scoreBooks)
        {
            if(scoreBook.getOwner().equals(student))
            {
                return scoreBook;
            }
        }
        return null;
    }

    public void createScoreBook(Student student)
    {
        ScoreBook scoreBook = new ScoreBook(student);
        for(AssignmentDescription ad : assignmentDescriptions)
        {
            scoreBook.createAssignmentScore(ad);
        }
        this.scoreBooks.add(scoreBook);
    }

    public boolean addAssignment(String title, String description, double maxScore, DateTime dateTime)
    {
        AssignmentDescription assignmentDescription = new AssignmentDescription();
        assignmentDescription.setTitle(title);
        assignmentDescription.setDescription(description);
        assignmentDescription.setMaxScore(maxScore);
        assignmentDescription.setDueDate(dateTime);
        if(isAssignmentDescriptionExists(assignmentDescription))
        {
            return false;
        }
        else
        {
            assignmentDescriptions.add(assignmentDescription);
            for (ScoreBook ab : scoreBooks)
            {
                ab.createAssignmentScore(assignmentDescription);
            }
            return true;
        }
    }

    private boolean isAssignmentDescriptionExists(AssignmentDescription assignmentDescription)
    {
        for(AssignmentDescription ad : assignmentDescriptions)
        {
            if(ad.equals(assignmentDescription))
            {
                return true;
            }
        }
        return false;
    }

    public void drop(Student student)
    {
        for(ScoreBook scoreBook : scoreBooks)
        {
            if(scoreBook.getOwner().equals(student))
            {
                scoreBooks.remove(scoreBook);
                break;
            }
        }
    }

    public boolean updateAssignmentScore(Student student, String title, double score)
    {
        ScoreBook scoreBook = getAssignmentBookByStudent(student);
        if(scoreBook == null)
        {
            return false;
        }
        else
        {
            return scoreBook.updateScoreByTitle(title, score);
        }
    }

    public Set<AssignmentDescription> getAssignmentDescriptions()
    {
        return assignmentDescriptions;
    }

    public Set<ScoreBook> getScoreBooks()
    {
        return scoreBooks;
    }
}
