package th.ac.kmitl.ce.ooad.cest.dao.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import th.ac.kmitl.ce.ooad.cest.entity.Course;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CourseDao implements ICourseDao {
    @Autowired
    private HibernateTemplate  hibernateTemplate;

    public void saveCourse(Course course) {
        hibernateTemplate.save(course);
    }

    public List<Course> findByCourseId(String courseId) {
        List<Course> courses = (List<Course>)hibernateTemplate.find("from Course c where c.courseId = ?", courseId);
        return courses;
    }

    public List<Course> findByCourseName(String courseName) {
        List<Course> courses = (List<Course>)hibernateTemplate.find("from Course c where c.courseName = ?", courseName);
        return courses;
    }

    public List<Course> findAllCourse() {
        List<Course> courses = (List<Course>)hibernateTemplate.find("from Course");
        return courses;
    }
}