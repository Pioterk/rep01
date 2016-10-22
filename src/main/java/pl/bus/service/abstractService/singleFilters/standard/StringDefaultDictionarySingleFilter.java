package pl.bus.service.abstractService.singleFilters.standard;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import org.springframework.stereotype.Component;
import pl.bus.service.abstractService.prototype.SingleFilterApplicableTypes;
import pl.bus.domain.dictionaries.proto.DefaultDictionary;
import pl.bus.service.abstractService.prototype.AbstractServiceSingleFilter;
import pl.bus.service.abstractService.prototype.BaseSingleFilter;

@Component
@BaseSingleFilter(types = @SingleFilterApplicableTypes(filterType = DefaultDictionary.class, qObjectType = EnumPath.class))
public class StringDefaultDictionarySingleFilter extends AbstractServiceSingleFilter {

    @SuppressWarnings("unchecked")
    @Override
    public BooleanExpression predicate(Object filterValue, Object qObjectValue) {
        return ((EnumPath) qObjectValue).eq(filterValue);
    }

}