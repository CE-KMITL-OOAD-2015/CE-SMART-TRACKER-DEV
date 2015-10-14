package th.ac.kmitl.ce.ooad.cest.entity;

import javax.persistence.*;

/**
 * Created by atsaw on 14/10/2558.
 */
@Entity
@Table(name="student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int studentDbId;
    private String studentId;
    private String firstName;
    private String lastName;

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
}
