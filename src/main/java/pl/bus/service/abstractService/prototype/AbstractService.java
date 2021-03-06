package pl.bus.service.abstractService.prototype;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.modelmapper.AbstractConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.bus.service.converters.ListPageConverter;
import pl.bus.service.converters.prototype.EntityConvertersPack;
import pl.bus.web.misc.ListPage;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @param <Entity>           Entity
 * @param <EntityDTO>        Entity Data Transfer Object (DTO)
 * @param <ListDTO>          Entity List Data Transfer Object (DTO)
 * @param <ListFilters>      List filter
 * @param <QEntity>          Entity Q-Class
 * @param <EntityID>         Entity ID
 * @param <EntityRepository> Entity repository
 */
public abstract class AbstractService<
        Entity,
        EntityDTO,
        ListDTO,
        ListFilters,
        QEntity extends EntityPathBase<Entity>,
        EntityID extends Serializable,
        EntityRepository extends JpaRepository<Entity, EntityID> & QueryDslPredicateExecutor<Entity>
        > {

    @Inject
    private EntityConvertersPack entityConvertersPack;

    @Inject
    private SingleFiltersPack singleFiltersPack;

    @Inject
    private ListPageConverter listPageConverter;

    public ResponseEntity<EntityDTO> get(EntityID id) {
        Entity entity = getRepository().findOne(id);

        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!getGetAuthorisationPredicate().test(entity)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!getGetPreValidationPredicate().test(entity)) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }

        EntityDTO entityDTO = getGetProcessingFunction().apply(
                getGetPreProcessingFunction().apply(entity)
        );

        if (!getGetPostValidationPredicate().test(entityDTO)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        entityDTO = getGetPostProcessingFunction().apply(entityDTO);

        return new ResponseEntity<>(entityDTO, HttpStatus.OK);
    }

    public ResponseEntity<String> save(EntityDTO entityDTO) {
        if (!getSaveAuthorisationPredicate().test(entityDTO)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!getSavePreValidationPredicate().test(entityDTO)) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }

        Entity entity = getSaveProcessingFunction().apply(
                getSavePreProcessingFunction().apply(entityDTO)
        );

        if (!getSavePostValidationPredicate().test(entity)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (getRepository().save(getSavePostProcessingFunction().apply(entity)) == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ListPage<ListDTO> get(Integer page, Integer limit, String sortBy, String filters) {
        QEntity user = getEQ();
        Page<Entity> entityPage = null;
        try {
            entityPage = getRepository().findAll(
                    applyFilters(filters, user),
                    new QPageRequest(page - 1,
                            limit,
                            applySorting(sortBy, user)
                    )
            );
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return listPageConverter.convert(entityPage, getListDtoClass());
    }

    protected abstract EntityRepository getRepository();

    protected abstract Boolean isFilterClassExtended();

    protected abstract Class<Entity> getEntityClass();

    protected abstract Class<EntityDTO> getDtoClass();

    protected abstract Class<ListDTO> getListDtoClass();

    protected abstract Class<ListFilters> getListFilterClass();

    protected Predicate<EntityDTO> getSavePreValidationPredicate() {
        return entityDTO -> true;
    }

    protected Predicate<Entity> getSavePostValidationPredicate() {
        return entityDTO -> true;
    }

    protected Predicate<EntityDTO> getSaveAuthorisationPredicate() {
        return entityDTO -> true;
    }

    protected UnaryOperator<Entity> getSavePostProcessingFunction() {
        return entity -> entity;
    }

    protected Function<EntityDTO, Entity> getSaveProcessingFunction() {
        return entityDTO -> entityConvertersPack.getPreparedModelMapper().map(entityDTO, getEntityClass());
    }

    protected UnaryOperator<EntityDTO> getSavePreProcessingFunction() {
        return entityDTO -> entityDTO;
    }

    protected Predicate<Entity> getGetPreValidationPredicate() {
        return entity -> true;
    }

    protected Predicate<EntityDTO> getGetPostValidationPredicate() {
        return entityDTO -> true;
    }

    protected Predicate<Entity> getGetAuthorisationPredicate() {
        return entity -> true;
    }

    protected UnaryOperator<EntityDTO> getGetPostProcessingFunction() {
        return entityDTO -> entityDTO;
    }

    protected Function<Entity, EntityDTO> getGetProcessingFunction() {
        return entity -> entityConvertersPack.getPreparedModelMapper().map(entity, getDtoClass());
    }

    protected UnaryOperator<Entity> getGetPreProcessingFunction() {
        return entity -> entity;
    }

    protected List<AbstractServiceSingleFilter> getAbstractServiceSingleFilters() {
        return singleFiltersPack.getFullEntityAbstractServiceSingleFiltersPack();
    }

    protected List<BooleanExpression> getPreFilters() {
        return Collections.emptyList();
    }

    protected List<AbstractConverter> getConverters() {
        return entityConvertersPack.getFullEntityConvertersPack();
    }

    protected abstract QEntity getEQ();

    private OrderSpecifier<?> applySorting(String sortBy, QEntity qObject) throws NoSuchFieldException, IllegalAccessException {
        return applySortingSupporter(
                sortBy.startsWith("-"),
                ((ComparableExpressionBase<?>) qObject.getClass().getDeclaredField(sortBy.substring(1)).get(qObject))
        );
    }

    private OrderSpecifier<?> applySortingSupporter(boolean isDesc, ComparableExpressionBase<?> comparableExpressionBase) {
        return isDesc ? comparableExpressionBase.desc() : comparableExpressionBase.asc();
    }

    private BooleanExpression applyFilters(String filtersJson, QEntity qObject) throws NoSuchFieldException, IllegalAccessException {
        BooleanExpressionWrapper pWrapper = new BooleanExpressionWrapper();
        pWrapper.predicate = qObject.isNotNull();

        for (BooleanExpression preFilter : getPreFilters()) {
            pWrapper.predicate = pWrapper.predicate.and(preFilter);
        }

        if (filtersJson.length() == 0) {
            return pWrapper.predicate;
        }

        ListFilters filters;

        try {
            filters = new ObjectMapper().readValue(filtersJson, getListFilterClass());

            Stream.concat(
                    Arrays.stream(filters.getClass().getDeclaredFields()),
                    getExtendedFields(filters)
            ).filter(
                    field -> extractValueFromField(field, filters) != null
            ).forEach(
                    field -> getAbstractServiceSingleFilters()
                            .stream()
                            .filter(filter -> filter.isApplicable(
                                    extractValueFromField(field, filters),
                                    extractValueFromField(
                                            extractFieldFromObject(
                                                    qObject,
                                                    field.getName()
                                            ),
                                            qObject
                                    ))
                            )
                            .forEach(filter -> pWrapper.predicate = filter.predicate(
                                    pWrapper.predicate,
                                    extractValueFromField(field, filters),
                                    extractValueFromField(
                                            extractFieldFromObject(
                                                    qObject,
                                                    field.getName()
                                            ),
                                            qObject
                                    ))
                            )
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pWrapper.predicate;
    }

    private Stream<Field> getExtendedFields(ListFilters filters) {
        if (isFilterClassExtended()) {
            return Arrays.stream(filters.getClass().getSuperclass().getDeclaredFields());
        }
        return Collections.<Field>emptyList().stream();
    }

    private Object extractValueFromField(Field field, Object object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Field extractFieldFromObject(Object clazz, String name) {
        try {
            return clazz.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class BooleanExpressionWrapper {
        private BooleanExpression predicate;
    }

}
