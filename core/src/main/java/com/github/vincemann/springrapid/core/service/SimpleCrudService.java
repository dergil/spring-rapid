package com.github.vincemann.springrapid.core.service;

import com.github.vincemann.aoplog.api.AopLoggable;
import com.github.vincemann.aoplog.api.LogConfig;
import com.github.vincemann.aoplog.api.LogInteraction;
import com.github.vincemann.aoplog.api.UltimateTargetClassAware;
import com.github.vincemann.springrapid.core.model.IdentifiableEntity;
import com.github.vincemann.springrapid.core.service.exception.BadEntityException;
import com.github.vincemann.springrapid.core.service.exception.EntityNotFoundException;
import com.github.vincemann.springrapid.core.slicing.components.ServiceComponent;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

@ServiceComponent
@LogInteraction
@LogConfig(ignoreGetters = true,ignoreSetters = true,logAllChildrenMethods = true)
public interface SimpleCrudService<E extends IdentifiableEntity<Id>,Id extends Serializable>
        extends AopLoggable, UltimateTargetClassAware{

        Class<E> getEntityClass();

        Optional<E> findById(Id id) throws BadEntityException;

        /**
         * If full is false:
         * only non null members of @param entity will be taken into consideration for updating the database entity.
         * If full is true:
         * all members of @param entity will be taken into consideration for updating the database entity.
         * @param entity
         * @return updated (database) entity
         */
        E update(E entity, Boolean full) throws EntityNotFoundException, BadEntityException;

        E save(E entity) throws  BadEntityException;

        Set<E> findAll();

        void deleteById(Id id) throws EntityNotFoundException, BadEntityException;
}