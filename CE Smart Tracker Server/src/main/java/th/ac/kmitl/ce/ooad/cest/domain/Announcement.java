package th.ac.kmitl.ce.ooad.cest.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;

@Entity
public class Announcement
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    private Teacher announcer;
    private final DateTime announceDate;

    public Announcement()
    {
        announceDate = new DateTime();
    }

    public Announcement(String title, String description, Teacher announcer)
    {
        this.title = title;
        this.description = description;
        this.announcer = announcer;
        announceDate = new DateTime();
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Teacher getAnnouncer()
    {
        return announcer;
    }

    public void setAnnouncer(Teacher announcer)
    {
        this.announcer = announcer;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getAnnounceDate()
    {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
        return fmt.print(announceDate);
    }
}
