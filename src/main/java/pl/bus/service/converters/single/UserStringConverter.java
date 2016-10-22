package pl.bus.service.converters.single;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import pl.bus.domain.User;
import pl.bus.service.converters.prototype.EntityConverter;

@Component
@EntityConverter
public class UserStringConverter extends AbstractConverter<User, String> {
    @Override
    protected String convert(User user) {
        if (user == null) {
            return null;
        }
        return user.getFirstName() + " " + user.getLastName() + " (" + user.getLogin() + ")";
    }
}