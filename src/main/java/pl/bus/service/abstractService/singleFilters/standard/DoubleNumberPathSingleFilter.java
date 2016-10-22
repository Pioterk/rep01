package pl.bus.service.abstractService.singleFilters.standard;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.stereotype.Component;
import pl.bus.service.abstractService.prototype.AbstractServiceSingleFilter;
import pl.bus.service.abstractService.prototype.BaseSingleFilter;
import pl.bus.service.abstractService.prototype.SingleFilterApplicableTypes;

@Component
@BaseSingleFilter(types = @SingleFilterApplicableTypes(filterType = Double.class, qObjectType = NumberPath.class))
public class DoubleNumberPathSingleFilter extends AbstractServiceSingleFilter {

    @SuppressWarnings("unchecked")
    @Override
    public BooleanExpression predicate(Object filterValue, Object qObjectValue) {
        return ((NumberPath<Double>) qObjectValue).eq((Double) filterValue);
    }

}
