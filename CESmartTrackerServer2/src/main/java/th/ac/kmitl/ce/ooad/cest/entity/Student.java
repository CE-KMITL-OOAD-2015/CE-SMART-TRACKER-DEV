package th.ac.kmitl.ce.ooad.cest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int studentDbId;
    private String studentId;
    private String firstName;
    private String lastName;
    private String faculty;
    private String department;

    @JsonIgnore
    @ManyToMany(mappedBy="enrolledStudents")
    private Set<Course> enrolledCourses = new HashSet<Course>();

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Student))
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }

        Student student2 = (Student) obj;
        if(this.studentId == student2.getStudentId())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public int getStudentDbId() {
        return studentDbId;
    }

    public void setStudentDbId(int studentDbId) {
        this.studentDbId = studentDbId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setEnrolledCourses(Set<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

}
