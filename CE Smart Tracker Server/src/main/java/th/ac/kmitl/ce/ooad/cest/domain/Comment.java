package th.ac.kmitl.ce.ooad.cest.domain;

import javax.persistence.*;

@Entity
public class Comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String message;
    @ManyToOne
    private Student owner;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Student getOwner()
    {
        return owner;
    }

    public void setOwner(Student owner)
    {
        this.owner = owner;
    }
}
