package pl.bus.service.dictionary.implementation;

import org.springframework.stereotype.Service;
import pl.bus.domain.dictionaries.UserStatus;
import pl.bus.service.dictionary.DictionaryProvider;
import pl.bus.service.dictionary.DictionaryProviderFor;

import java.util.Arrays;
import java.util.List;

@Service
@DictionaryProviderFor("userStatus")
public class UserStatusDictionaryProvider implements DictionaryProvider {
    @Override
    public List loadProvider() {
        return Arrays.asList(UserStatus.values());
    }
}
