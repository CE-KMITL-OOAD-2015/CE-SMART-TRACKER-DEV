package th.ac.kmitl.ce.ooad.cest.domain;

import javax.persistence.*;

@Entity
public class Rating
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int point;
    @ManyToOne
    private Student owner;

    public int getPoint()
    {
        return point;
    }

    public void setPoint(int point)
    {
        this.point = point;
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
