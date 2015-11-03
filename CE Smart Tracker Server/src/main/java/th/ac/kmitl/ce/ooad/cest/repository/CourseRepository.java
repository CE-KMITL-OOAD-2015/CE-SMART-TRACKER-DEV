package th.ac.kmitl.ce.ooad.cest.repository;

import org.springframework.data.repository.CrudRepository;
import th.ac.kmitl.ce.ooad.cest.domain.Course;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Long>{
    Course findFirstByCourseId(String courseId);
    List<Course> findByCourseNameContainingOrCourseIdContaining(String courseName, String courseId);
}
