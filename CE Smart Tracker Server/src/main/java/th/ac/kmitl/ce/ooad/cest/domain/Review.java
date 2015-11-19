package th.ac.kmitl.ce.ooad.cest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Review
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<Rating> ratings = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Comment> comments = new ArrayList<>();

    public void addRating(Student student, int point)
    {
        // limit point to 0 - 10
        if(point > 10)
            point = 10;
        else if(point < 0)
            point = 0;

        // update rating if already rated
        for(Rating rating : ratings)
        {
            if(rating.getOwner().getUsername().equals(student.getUsername()))
            {
                rating.setPoint(point);
                return;
            }
        }

        // otherwise create new rating object
        Rating rating = new Rating();
        rating.setOwner(student);
        rating.setPoint(point);
        ratings.add(rating);
    }

    public boolean isCommented(Student student)
    {
        for(Comment comment : comments)
        {
            if(comment.getOwner().getUsername().equals(student.getUsername()))
            {
                return true;
            }
        }
        return false;
    }

    public double getAverageRating()
    {
        int sum = 0;
        int count = 0;
        for(Rating rating : ratings)
        {
            sum += rating.getPoint();
            count += 1;
        }
        if(count > 0)
        {
            return ((double)sum)/count;
        }
        else
        {
            return 0;
        }
    }

    public void addComment(Student student, String message)
    {
        Comment comment = new Comment();
        comment.setOwner(student);
        comment.setMessage(message);
        comments.add(comment);
    }

    public List<Comment> getComments()
    {
        return comments;
    }
}
