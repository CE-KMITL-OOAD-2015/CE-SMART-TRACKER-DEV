package th.ac.kmitl.ce.ooad.cest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Enrollment
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    private Set<Student> enrolledStudents = new HashSet<>();

    public void enroll(Student student)
    {
        enrolledStudents.add(student);
    }

    public void drop(Student student)
    {
        enrolledStudents.remove(student);
    }

    public boolean isEnrolledBy(Student student)
    {
        return enrolledStudents.contains(student);
    }

    public Set<Student> getEnrolledStudents()
    {
        return enrolledStudents;
    }
}
