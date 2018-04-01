package pri.holysu.springcloud.microserviceprovideruserauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pri.holysu.springcloud.microserviceprovideruserauth.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
