package codefod.com.springbootmentor.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission implements Serializable {

    private Long id;
    private Permission permission;
    private Role role;
}
