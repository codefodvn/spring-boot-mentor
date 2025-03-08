package codefod.com.springbootmentor.repository;

import codefod.com.springbootmentor.entity.Role;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository {

    Set<Role> findAllByIdIn(Set<Long> ids);
}
