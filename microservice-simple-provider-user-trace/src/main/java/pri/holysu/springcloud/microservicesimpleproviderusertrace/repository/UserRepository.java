package pri.holysu.springcloud.microservicesimpleproviderusertrace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pri.holysu.springcloud.microservicesimpleproviderusertrace.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
