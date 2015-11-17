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
@Table(name = "course")
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
    @ManyToMany(cascade = {CascadeType.ALL})
    private Set<Student> enrolledStudents = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    private Set<Teacher> teachers = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<AssignmentBook> assignmentBooks = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<AssignmentDescription> assignmentDescriptions = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<Rating> ratings = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL})
    private List<Announcement> announcements = new ArrayList<>();

    public void addAnnouncement(Teacher teacher, String title, String description)
    {
        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setDescription(description);
        announcement.setAnnouncer(teacher);
        announcements.add(announcement);
    }

    public void enroll(Student student)
    {
        enrolledStudents.add(student);
        AssignmentBook assignmentBook = new AssignmentBook(student);
        for(AssignmentDescription ad : assignmentDescriptions)
        {
            assignmentBook.createAssignment(ad);
        }
        this.assignmentBooks.add(assignmentBook);
    }

    public boolean addAssignment(String title, String description, double maxScore, DateTime dateTime)
    {
        AssignmentDescription assignmentDescription = new AssignmentDescription();
        assignmentDescription.setTitle(title);
        assignmentDescription.setDescription(description);
        assignmentDescription.setMaxScore(maxScore);
        assignmentDescription.setDueDate(dateTime);
        if(isAssignmentDescriptionExists(assignmentDescription))
        {
            return false;
        }
        else
        {
            assignmentDescriptions.add(assignmentDescription);
            for (AssignmentBook ab : assignmentBooks)
            {
                ab.createAssignment(assignmentDescription);
            }
            return true;
        }
    }

    public boolean isEnrolled(Student student)
    {
        return enrolledStudents.contains(student);
    }

    public boolean isAssignmentDescriptionExists(AssignmentDescription assignmentDescription)
    {
        for(AssignmentDescription ad : assignmentDescriptions)
        {
            if(ad.equals(assignmentDescription))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isTeaching(Teacher teacher)
    {
        return teachers.contains(teacher);
    }

    public void addRating(Student student, int point)
    {
        // limit point to 0 - 10
        if(point > 10)
            point = 10;
        else if(point < 0)
            point = 0;

        // update rating if already rated
        for(Rating rating : ratings)
        {
            if(rating.getOwner().getUsername().equals(student.getUsername()))
            {
                rating.setPoint(point);
                return;
            }
        }

        // otherwise create new rating object
        Rating rating = new Rating();
        rating.setOwner(student);
        rating.setPoint(point);
        ratings.add(rating);
    }

    public boolean isRated(Student student)
    {
        for(Rating rating : ratings)
        {
            if(rating.getOwner().getUsername().equals(student.getUsername()))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isCommented(Student student)
    {
        for(Comment comment : comments)
        {
            if(comment.getOwner().getUsername().equals(student.getUsername()))
            {
                return true;
            }
        }
        return false;
    }

    public double getAverageRating()
    {
        int sum = 0;
        int count = 0;
        for(Rating rating : ratings)
        {
            sum += rating.getPoint();
            count += 1;
        }
        if(count > 0)
        {
            return ((double)sum)/count;
        }
        else
        {
            return 0;
        }
    }

    public void addComment(Student student, String message)
    {
        Comment comment = new Comment();
        comment.setOwner(student);
        comment.setMessage(message);
        comments.add(comment);
    }

    public void drop(Student student)
    {
        for(AssignmentBook assignmentBook : assignmentBooks)
        {
            if(assignmentBook.getOwner().equals(student))
            {
                assignmentBooks.remove(assignmentBook);
                break;
            }
        }
        enrolledStudents.remove(student);
    }

    public boolean updateAssignmentScore(Student student, String title, double score)
    {
        AssignmentBook assignmentBook = getAssignmentBookByStudent(student);
        if(assignmentBook == null)
        {
            return false;
        }
        else
        {
            return assignmentBook.updateScoreByTitle(title, score);
        }
    }

    public AssignmentBook getAssignmentBookByStudent(Student student)
    {
        if(student == null)
        {
            return null;
        }

        for(AssignmentBook assignmentBook : assignmentBooks)
        {
            if(assignmentBook.getOwner().equals(student))
            {
                return assignmentBook;
            }
        }
        return null;
    }

    public List<Announcement> getAnnouncements()
    {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements)
    {
        this.announcements = announcements;
    }

    public void addTeacher(Teacher teacher)
    {
        teachers.add(teacher);
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

    public Set<Student> getEnrolledStudents()
    {
        return enrolledStudents;
    }

    public void setEnrolledStudents(Set<Student> enrolledStudents)
    {
        this.enrolledStudents = enrolledStudents;
    }

    public Set<Teacher> getTeachers()
    {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers)
    {
        this.teachers = teachers;
    }

    public List<Comment> getComments()
    {
        return comments;
    }

    public void setComments(List<Comment> comments)
    {
        this.comments = comments;
    }

    public Set<AssignmentBook> getAssignmentBooks()
    {
        return assignmentBooks;
    }

    public Set<AssignmentDescription> getAssignmentDescriptions()
    {
        return assignmentDescriptions;
    }

    /*
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
    */

    /*
        public int getCourseDbId() {
            return courseDbId;
        }

        public void setCourseDbId(int courseDbId) {
            this.courseDbId = courseDbId;
        }

        public int getDb_id() {
            return courseDbId;
        }
    */
}
