package th.ac.kmitl.ce.ooad.cest.repository;

import org.springframework.data.repository.CrudRepository;
import th.ac.kmitl.ce.ooad.cest.domain.User;

public interface UserRepository extends CrudRepository<User, Long>{
    User findFirstByUsername(String username);
    User findFirstBySessionId(String sessionId);
    User findFirstByFacebookId(String facebookId);
}
