package pl.bus.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.security.authentication.encoding.BasePasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.bus.dao.repository.UserRepository;
import pl.bus.domain.QUser;
import pl.bus.domain.User;
import pl.bus.security.authorisation.PermissionManager;
import pl.bus.service.abstractService.prototype.AbstractService;
import pl.bus.service.exception.DuplicatedEntityException;
import pl.bus.service.exception.EntityNotFoundException;
import pl.bus.web.dto.UserDTO;
import pl.bus.web.dto.UserListDTO;
import pl.bus.web.misc.UserListFilters;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

@Service
@Transactional
public class UserService extends AbstractService<User, UserDTO, UserListDTO, UserListFilters, QUser, Long, UserRepository> {

    @Inject
    private PermissionManager permissionManager;

    @Inject
    private UserRepository repository;

    @Inject
    private BasePasswordEncoder passwordEncoder;

    @Override
    protected UserRepository getRepository() {
        return repository;
    }

    @Override
    protected Boolean isFilterClassExtended() {
        return true;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Class<UserDTO> getDtoClass() {
        return UserDTO.class;
    }

    @Override
    protected Class<UserListDTO> getListDtoClass() {
        return UserListDTO.class;
    }

    @Override
    protected Class<UserListFilters> getListFilterClass() {
        return UserListFilters.class;
    }

    @Override
    protected List<BooleanExpression> getPreFilters() {
        return Collections.emptyList();
    }

    @Override
    protected QUser getEQ() {
        return QUser.user;
    }

    @Override
    protected UnaryOperator<UserDTO> getGetPostProcessingFunction() {
        return (userDTO) -> {
            userDTO.setPassword(null);
            return userDTO;
        };
    }

    @Override
    protected UnaryOperator<User> getSavePostProcessingFunction() {
        return (user) -> {
            if (user.getId() == null) {
                if (repository.findOneByLogin(user.getLogin()) != null) {
                    throw new DuplicatedEntityException("User with this login already exists: " + user.getLogin());
                }
            } else {
                User originUser = repository.findOne(user.getId());
                if (originUser == null) {
                    throw new EntityNotFoundException("User with this ID was not found: " + user.getId());
                } else {
                    if (user.getPassword() == null) {
                        user.setPassword(originUser.getPassword());
                    } else {
                        user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
                    }
                    if (user.getAccountExpireDate() == null) {
                        user.setAccountExpireDate(originUser.getAccountExpireDate());
                    }
                }
            }
            return user;
        };
    }

}
