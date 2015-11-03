package th.ac.kmitl.ce.ooad.cest.repository;

import org.springframework.data.repository.CrudRepository;
import th.ac.kmitl.ce.ooad.cest.domain.Account;

public interface AccountRepository extends CrudRepository<Account, Long>{
    Account findFirstByUsername(String username);
}
