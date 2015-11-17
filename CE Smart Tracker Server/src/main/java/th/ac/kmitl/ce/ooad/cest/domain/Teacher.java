package th.ac.kmitl.ce.ooad.cest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="teacher")
public class Teacher extends User
{
    @JsonIgnore
    @ManyToMany(mappedBy="teachers")
    private Set<Course> teachingCourses = new HashSet<Course>();

    public Teacher()
    {
        super();
    }

    public Set<Course> getTeachingCourses() {
        return teachingCourses;
    }

    public void setTeachingCourses(Set<Course> teachingCourses) {
        this.teachingCourses = teachingCourses;
    }
}
