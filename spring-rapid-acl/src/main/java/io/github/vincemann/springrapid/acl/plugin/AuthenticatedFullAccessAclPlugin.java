package io.github.vincemann.springrapid.acl.plugin;

import io.github.vincemann.springrapid.acl.service.LocalPermissionService;
import io.github.vincemann.springrapid.core.model.IdentifiableEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;

@Slf4j
public class AuthenticatedFullAccessAclPlugin
        extends CleanUpAclPlugin<IdentifiableEntity<Serializable>,Serializable> {


    public AuthenticatedFullAccessAclPlugin(LocalPermissionService permissionService, MutableAclService mutableAclService) {
        super(permissionService, mutableAclService);
    }

    public void onAfterSave(IdentifiableEntity<Serializable> requestEntity, IdentifiableEntity<Serializable> returnedEntity) {
        saveFullPermissionForAuthenticatedOver(returnedEntity);
    }

    protected String findAuthenticatedUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //Nicht auslagern. MutableAclService macht das intern auch so -> use @MockUser(username="testUser") in tests
        if(authentication==null){
            throw new IllegalArgumentException("Authentication required");
        }
        return authentication.getName();
    }

    /**
     * The passed entity will have Administration rights about itself
     * @param entity
     */
    protected void saveFullPermissionForAuthenticatedOver(IdentifiableEntity<Serializable> entity){
        savePermissionForAuthenticatedOver(entity, BasePermission.ADMINISTRATION);
    }

    protected void savePermissionForAuthenticatedOver(IdentifiableEntity<Serializable> entity, Permission permission){
        String own = findAuthenticatedUsername();
        getPermissionService().addPermissionForUserOver(entity, permission,own);
    }
}