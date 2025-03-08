package codefod.com.springbootmentor.repository;

import codefod.com.springbootmentor.entity.Permission;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository {

    Set<Permission> findAllByIdIn(Set<Long> ids);

    List<Permission> findAll();
}
