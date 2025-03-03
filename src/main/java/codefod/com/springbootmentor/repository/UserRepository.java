package codefod.com.springbootmentor.repository;

import codefod.com.springbootmentor.entity.User;
import codefod.com.springbootmentor.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    User findByEmail(String email);

}
