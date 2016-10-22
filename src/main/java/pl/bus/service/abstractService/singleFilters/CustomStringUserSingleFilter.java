package pl.bus.service.abstractService.singleFilters;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;
import pl.bus.service.abstractService.prototype.AbstractServiceSingleFilter;
import pl.bus.service.abstractService.prototype.BaseSingleFilter;
import pl.bus.service.abstractService.prototype.SingleFilterApplicableTypes;
import pl.bus.domain.QUser;

@Component
@BaseSingleFilter(types = @SingleFilterApplicableTypes(filterType = String.class, qObjectType = QUser.class))
public class CustomStringUserSingleFilter extends AbstractServiceSingleFilter {

    @Override
    public BooleanExpression predicate(Object filterValue, Object qObjectValue) {
        return ((QUser) qObjectValue).firstName
                .concat(" ")
                .concat(((QUser) qObjectValue).lastName)
                .concat(" (")
                .concat(((QUser) qObjectValue).login)
                .concat(")")
                .contains((String) filterValue);
    }

}
