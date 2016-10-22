package pl.bus.service.converters.single;

import org.joda.time.LocalDateTime;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import pl.bus.service.converters.prototype.EntityConverter;

@Component
@EntityConverter
public class JodaLocalDateTimeBooleanConverter extends AbstractConverter<LocalDateTime, Boolean> {
    @Override
    protected Boolean convert(LocalDateTime localDate) {
        return localDate != null;
    }
}