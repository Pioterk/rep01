package pl.bus.service.abstractService.singleFilters.standard;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.stereotype.Component;
import pl.bus.service.abstractService.prototype.BaseSingleFilter;
import pl.bus.service.abstractService.prototype.SingleFilterApplicableTypes;
import pl.bus.service.abstractService.prototype.AbstractServiceSingleFilter;

@Component
@BaseSingleFilter(types = @SingleFilterApplicableTypes(filterType = String.class, qObjectType = StringPath.class))
public class StringStringPathSingleFilter extends AbstractServiceSingleFilter {

    @Override
    public BooleanExpression predicate(Object filterValue, Object qObjectValue) {
        return ((StringPath) qObjectValue).contains((String) filterValue);
    }

}
