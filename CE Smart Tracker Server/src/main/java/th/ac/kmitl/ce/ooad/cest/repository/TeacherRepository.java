package th.ac.kmitl.ce.ooad.cest.repository;

import org.springframework.data.repository.CrudRepository;
import th.ac.kmitl.ce.ooad.cest.domain.Teacher;


public interface TeacherRepository extends CrudRepository<Teacher, Long> {
    Teacher findFirstByUsername(String username);
    Teacher findFirstBySessionId(String sessionId);
}
