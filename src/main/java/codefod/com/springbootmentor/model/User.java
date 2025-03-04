package codefod.com.springbootmentor.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String username;
    private String password;
    private String email;
    private String name;
    private Boolean active;
    private String role;
    private String source;
}
