package th.ac.kmitl.ce.ooad.cest.dao;

import th.ac.kmitl.ce.ooad.cest.entity.Course;

import java.util.List;

public interface ICourseDao {
    public void saveCourse(Course course);
    public List<Course> findByCourseId(String courseId);
    public List<Course> findByCourseName(String courseName);
}
