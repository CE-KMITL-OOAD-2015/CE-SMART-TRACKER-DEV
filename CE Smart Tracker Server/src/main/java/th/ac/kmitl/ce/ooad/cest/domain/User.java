package th.ac.kmitl.ce.ooad.cest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User
{
    @Id
    @Column(nullable = false)
    private String username;
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    @JsonIgnore
    @Column(nullable = false)
    private String salt;
    @JsonIgnore
    private String sessionId;
    @JsonIgnore
    private String facebookId;
    @Column(nullable = false)
    protected String firstName;
    @Column(nullable = false)
    protected String lastName;
    @Column(nullable = false)
    protected Faculty faculty;
    protected String department;


    public User()
    {
        super();
    }

    /*
    public User(String username, String password, String salt, String studentId, String firstName, String lastName, String faculty, String department)
    {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.faculty = faculty;
        this.department = department;
    }
    */

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getFacebookId()
    {
        return facebookId;
    }

    public void setFacebookId(String facebookId)
    {
        this.facebookId = facebookId;
    }

    public Faculty getFaculty()
    {
        return faculty;
    }

    public void setFaculty(Faculty faculty)
    {
        this.faculty = faculty;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }
}
