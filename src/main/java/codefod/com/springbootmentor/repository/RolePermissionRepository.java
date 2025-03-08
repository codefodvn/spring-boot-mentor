package codefod.com.springbootmentor.repository;

import codefod.com.springbootmentor.entity.RolePermission;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository {

    Set<RolePermission> findAllByRoleIdIn(Set<Long> roleIds);
}
