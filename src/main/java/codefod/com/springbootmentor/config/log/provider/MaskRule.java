package codefod.com.springbootmentor.config.log.provider;

import com.github.skjolber.jsonfilter.JsonFilter;
import com.github.skjolber.jsonfilter.core.DefaultJsonLogFilterBuilder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MaskRule {

    JsonFilter filter = DefaultJsonLogFilterBuilder.createInstance()
            .withMaxStringLength(2048)
            .withAnonymize("$.username", "$.password", "$.accessToken", "$.refreshToken")
            .withAnonymize("$.*.username", "$.*.password", "$.*.accessToken", "$.*.refreshToken")
            .withMaxPathMatches(5)
            .withMaxSize(1024 * 1024)
            .build();

    public String apply(String input) {
        return maskMessage(input);
    }

    private String maskMessage(String message) {
        return filter.process(message);
    }
}