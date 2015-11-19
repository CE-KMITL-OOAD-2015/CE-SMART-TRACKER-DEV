package th.ac.kmitl.ce.ooad.cest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Course
{
    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int courseDbId;*/
    @Id
    @Column(nullable = false)
    private String courseId;
    @Column(nullable = false)
    private String courseName;
    @Column(nullable = false)
    private Faculty faculty;
    private String department;
    @Column(columnDefinition = "TEXT")
    private String description;
    private DayOfWeek courseDay;
    private String courseTime;

    @JsonIgnore
    @OneToOne(cascade = {CascadeType.ALL})
    private Enrollment enrollment = new Enrollment();

    @JsonIgnore
    @OneToOne(cascade = {CascadeType.ALL})
    private Assignment assignment = new Assignment();

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    private Set<Teacher> teachers = new HashSet<>();

    @JsonIgnore
    @OneToOne(cascade = {CascadeType.ALL})
    private Review review = new Review();

    /*@JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<Rating> ratings = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Comment> comments = new ArrayList<>();*/

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Announcement> announcements = new ArrayList<>();

    public void addAnnouncement(Teacher teacher, String title, String description)
    {
        Announcement announcement = new Announcement(title, description, teacher);
        announcements.add(announcement);
    }

    public void enroll(Student student)
    {
        assignment.createScoreBook(student);
        enrollment.enroll(student);
        student.getEnrolledCourses().add(this);
    }

    public void drop(Student student)
    {
        assignment.drop(student);
        enrollment.drop(student);
        student.getEnrolledCourses().remove(this);
    }

    public boolean isEnrolledBy(Student student)
    {
        return enrollment.isEnrolledBy(student);
    }

    public boolean isTeachingBy(Teacher teacher)
    {
        return teachers.contains(teacher);
    }

    public void addRating(Student student, int point)
    {
        review.addRating(student, point);
    }

    public boolean isCommented(Student student)
    {
        return review.isCommented(student);
    }

    public double getAverageRating()
    {
        return review.getAverageRating();
    }

    public void addComment(Student student, String message)
    {
        review.addComment(student, message);
    }

    public ScoreBook getAssignmentBookByStudent(Student student)
    {
        return this.assignment.getAssignmentBookByStudent(student);
    }

    public boolean addAssignment(String title, String description, double maxScore, DateTime dateTime)
    {
        return this.assignment.addAssignment(title, description, maxScore, dateTime);
    }

    public boolean updateAssignmentScore(Student student, String title, double score)
    {
        ScoreBook scoreBook = getAssignmentBookByStudent(student);
        if(scoreBook == null)
        {
            return false;
        }
        else
        {
            return scoreBook.updateScoreByTitle(title, score);
        }
    }

    @JsonIgnore
    public Set<AssignmentDescription> getAssignmentDescriptions()
    {
        return this.assignment.getAssignmentDescriptions();
    }

    @JsonIgnore
    public Set<ScoreBook> getScoreBooks()
    {
        return this.assignment.getScoreBooks();
    }

    @JsonIgnore
    public Set<Student> getEnrolledStudents()
    {
        return this.enrollment.getEnrolledStudents();
    }

    @JsonIgnore
    public Statistic getStatistic()
    {
        return new Statistic(this.assignment.getScoreBooks());
    }

    public List<Announcement> getAnnouncements()
    {
        return announcements;
    }

    public String getCourseId()
    {
        return courseId;
    }

    public void setCourseId(String courseId)
    {
        this.courseId = courseId;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

    public DayOfWeek getCourseDay()
    {
        return courseDay;
    }

    public void setCourseDay(DayOfWeek courseDay)
    {
        this.courseDay = courseDay;
    }

    public String getCourseTime()
    {
        return courseTime;
    }

    public void setCourseTime(String courseTime)
    {
        this.courseTime = courseTime;
    }

/*    public Set<Student> getEnrolledStudents()
    {
        return enrolledStudents;
    }

    public void setEnrolledStudents(Set<Student> enrolledStudents)
    {
        this.enrolledStudents = enrolledStudents;
    }*/

    public Set<Teacher> getTeachers()
    {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers)
    {
        this.teachers = teachers;
    }

    @JsonIgnore
    public List<Comment> getComments()
    {
        return review.getComments();
    }

}
