package pl.bus.service.converters.single;

import org.joda.time.LocalDate;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import pl.bus.service.converters.prototype.EntityConverter;

@Component
@EntityConverter
public class JodaLocalDateBooleanConverter extends AbstractConverter<LocalDate, Boolean> {
    @Override
    protected Boolean convert(LocalDate localDate) {
        return localDate != null;
    }
}