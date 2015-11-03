package th.ac.kmitl.ce.ooad.cest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="student")
public class Student extends Account{
    @Column(nullable=false)
    private int studentId;
    //@Id
    ///@GeneratedValue(strategy = GenerationType.AUTO)
    //private int studentDbId;

    @JsonIgnore
    @ManyToMany(mappedBy="enrolledStudents")
    private Set<Course> enrolledCourses = new HashSet<Course>();

    public Student()
    {
        super();
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    /*
    public Student(String username, String password, String salt, String studentId, String firstName, String lastName, String faculty, String department)
    {
        super(username, password, salt, studentId, firstName, lastName, faculty, department);
    }
    */

    /*
    public int getStudentDbId() {
        return studentDbId;
    }

    public void setStudentDbId(int studentDbId) {
        this.studentDbId = studentDbId;
    }
    */


    public Set<Course> getEnrolledCourses() {
        return enrolledCourses;
    }



    public void setEnrolledCourses(Set<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

}