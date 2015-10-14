package th.ac.kmitl.ce.ooad.cest.entity;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int courseDbId;
    private String courseId;
    private String courseName;
    private String description;

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Course))
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }

        Course course2 = (Course) obj;
        if(this.courseId == course2.getCourseId())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setCourseDbId(int courseDbId) {
        this.courseDbId = courseDbId;
    }

    public int getDb_id() {
        return courseDbId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
