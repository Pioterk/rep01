package pl.bus.service.converters.single;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import pl.bus.service.converters.prototype.EntityConverter;

@Component
@EntityConverter
public class JodaLocalDateLocalDateTimeConverter extends AbstractConverter<LocalDate, LocalDateTime> {
    @Override
    protected LocalDateTime convert(LocalDate localDate) {
        return localDate == null ? null : new LocalDateTime(localDate);
    }
}
