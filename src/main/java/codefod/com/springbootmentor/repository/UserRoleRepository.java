package codefod.com.springbootmentor.repository;

import codefod.com.springbootmentor.entity.UserRole;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository {

    Set<UserRole> findByUser_UserId(Long userId);
}
