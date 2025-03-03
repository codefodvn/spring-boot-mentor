package codefod.com.springbootmentor.config.log.provider;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import net.logstash.logback.composite.JsonWritingUtils;
import net.logstash.logback.composite.loggingevent.MessageJsonProvider;

public class MaskingMessageProvider extends MessageJsonProvider {

    MaskRule maskRule = new MaskRule();

    @Override
    public void writeTo(JsonGenerator generator, ILoggingEvent event) throws IOException {
        JsonWritingUtils.writeStringField(generator, getFieldName(),
                maskRule.apply(event.getFormattedMessage()));
    }

}