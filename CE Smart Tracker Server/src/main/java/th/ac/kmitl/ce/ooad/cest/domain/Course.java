package th.ac.kmitl.ce.ooad.cest.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
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
    private String faculty;
    private String department;
    private String description;
    private String courseDay;
    private String courseTime;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    /*
    @JoinTable(name = "course_student",
            joinColumns = {@JoinColumn(name = "courseId")},
            inverseJoinColumns = {@JoinColumn(name = "username")})
    */
    private Set<Student> enrolledStudents = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    /*
    @JoinTable(name = "course_teacher",
            joinColumns = {@JoinColumn(name = "courseId")},
            inverseJoinColumns = {@JoinColumn(name = "username")})
    */
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

    public void enroll(Student student)
    {
        enrolledStudents.add(student);
        AssignmentBook assignmentBook = new AssignmentBook();
        assignmentBook.setOwner(student);
        for(AssignmentDescription ad : this.assignmentDescriptions)
        {
            assignmentBook.addAssignment(createAssignment(ad));
        }
        this.assignmentBooks.add(assignmentBook);
    }

    public boolean addAssignment(String title, String description, float maxScore)
    {
        AssignmentDescription assignmentDescription = new AssignmentDescription();
        assignmentDescription.setTitle(title);
        assignmentDescription.setDescription(description);
        assignmentDescription.setMaxScore(maxScore);
        if(isAssignmentDescriptionExists(assignmentDescription))
        {
            return false;
        }
        else
        {
            assignmentDescriptions.add(assignmentDescription);
            for (AssignmentBook ab : assignmentBooks)
            {
                ab.addAssignment(createAssignment(assignmentDescription));
            }
            return true;
        }
    }

    private Assignment createAssignment(AssignmentDescription assignmentDescription)
    {
        Assignment assignment = new Assignment();
        assignment.setAssignmentDescription(assignmentDescription);
        assignment.setScore(0);
        return assignment;
    }

    public boolean isEnrolled(Student student)
    {
        if(enrolledStudents.contains(student))
        {
            return true;
        }
        else
        {
            return false;
        }
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

    public String getFaculty()
    {
        return faculty;
    }

    public void setFaculty(String faculty)
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

    public String getCourseDay()
    {
        return courseDay;
    }

    public void setCourseDay(String courseDay)
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
