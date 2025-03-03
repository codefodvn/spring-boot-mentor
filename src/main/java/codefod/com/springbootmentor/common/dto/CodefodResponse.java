package codefod.com.springbootmentor.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CodefodResponse<T> {

    private Integer httpStatus;
    private String errorCode;
    private String message;
    private String traceId;
    private @Builder.Default Boolean success = Boolean.FALSE;

    private String code;
    private T data;

    public static <V> ResponseEntity<CodefodResponse<V>> ok(V model) {
        CodefodResponse<V> response = new CodefodResponse<>();
        response.success = true;
        response.data = model;
        return ResponseEntity.ok(response);
    }
}