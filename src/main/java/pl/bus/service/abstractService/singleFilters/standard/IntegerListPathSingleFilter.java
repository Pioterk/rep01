package pl.bus.service.abstractService.singleFilters.standard;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ListPath;
import org.springframework.stereotype.Component;
import pl.bus.service.abstractService.prototype.AbstractServiceSingleFilter;
import pl.bus.service.abstractService.prototype.BaseSingleFilter;
import pl.bus.service.abstractService.prototype.SingleFilterApplicableTypes;

@Component
@BaseSingleFilter(types = @SingleFilterApplicableTypes(filterType = Integer.class, qObjectType = ListPath.class))
public class IntegerListPathSingleFilter extends AbstractServiceSingleFilter {

    @SuppressWarnings("unchecked")
    @Override
    public BooleanExpression predicate(Object filterValue, Object qObjectValue) {
        return ((ListPath<?, ?>) qObjectValue).size().eq((Integer) filterValue);
    }

}
