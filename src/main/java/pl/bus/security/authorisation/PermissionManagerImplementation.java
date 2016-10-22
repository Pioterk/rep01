package pl.bus.security.authorisation;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

@Service
public class PermissionManagerImplementation implements PermissionManager {

    @Inject
    private EntityManager entityManager;

}
