package th.ac.kmitl.ce.ooad.cest.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="course")
public class Course {
    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int courseDbId;*/
    @Id
    @Column(nullable=false)
    private String courseId;
    @Column(nullable=false)
    private String courseName;
    @Column(nullable=false)
    private String faculty;
    private String department;
    private String description;
    private String courseDay;
    private String courseTime;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="course_student",
            joinColumns={@JoinColumn(name="courseId")},
            inverseJoinColumns={@JoinColumn(name="username")})
    private Set<Student> enrolledStudents = new HashSet<Student>();

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="course_teacher",
            joinColumns={@JoinColumn(name="courseId")},
            inverseJoinColumns={@JoinColumn(name="username")})
    private Set<Teacher> teachers = new HashSet<Teacher>();

    public void enroll(Student student)
    {
        enrolledStudents.add(student);
    }

    public void addTeacher(Teacher teacher)
    {
        teachers.add(teacher);
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

    public String getCourseDay() {
        return courseDay;
    }

    public void setCourseDay(String courseDay) {
        this.courseDay = courseDay;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public Set<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(Set<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }
}
