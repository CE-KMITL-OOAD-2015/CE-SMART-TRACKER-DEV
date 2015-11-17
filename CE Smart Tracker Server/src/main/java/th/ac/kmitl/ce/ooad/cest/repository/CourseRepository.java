package th.ac.kmitl.ce.ooad.cest.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import th.ac.kmitl.ce.ooad.cest.domain.Course;
import th.ac.kmitl.ce.ooad.cest.domain.Faculty;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Long>{
    Course findFirstByCourseId(String courseId);
    Course findFirstByCourseName(String courseName);
    List<Course> findByCourseNameContainingOrCourseIdContainingOrderByCourseNameAsc(String courseName, String courseId);
    List<Course> findByFacultyOrderByCourseNameAsc(Faculty faculty);
    List<Course> findByDepartmentOrderByCourseNameAsc(String department);

    @Query("select c from Course c where c.department = ?2 or c.department = '' and c.faculty = ?1 order by c.courseName")
    List<Course> findByFacultyAndDepartmentOrNone(Faculty faculty, String department);
}
