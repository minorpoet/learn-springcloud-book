package pri.holysu.springcloud.microserviceprovideruser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pri.holysu.springcloud.microserviceprovideruser.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
