package pl.bus.service.converters.single;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import pl.bus.service.converters.prototype.EntityConverter;

import java.util.List;

@Component
@EntityConverter
public class ListIntegerConverter extends AbstractConverter<List<?>, Integer> {
    @Override
    protected Integer convert(List<?> list) {
        return list == null ? 0 : list.size();
    }
}