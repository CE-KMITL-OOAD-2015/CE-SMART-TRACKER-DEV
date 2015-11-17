package th.ac.kmitl.ce.ooad.cest.repository;

import org.springframework.data.repository.CrudRepository;
import th.ac.kmitl.ce.ooad.cest.domain.Student;
import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Long> {
    List<Student> findByStudentId(int studentId);
    Student findFirstByUsername(String username);
    Student findFirstBySessionId(String sessionId);
    Student findFirstByFacebookId(String facebookId);
}
