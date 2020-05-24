package org.example.authorize.component.httpdefault;

import lombok.extern.slf4j.Slf4j;
import org.example.authorize.component.httpdefault.dtoconfig.*;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.exception.EntityNotFoundException;
import org.example.authorize.exception.InvalidEntityException;
import org.example.authorize.exception.SaveEntityException;
import org.example.authorize.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

/**
 * Default Service.
 *
 * @param <T> Generic of entity
 */
@Slf4j
public class DefaultHttpService<T extends Audit<?>> {

    private DefaultHttpRepository<T> repository;

    /**
     * Create and save new entity.
     *
     * @param entityDTO DTO of entity
     * @return response DTO of entity
     */
    protected Object createAndSaveEntity(Object entityDTO) {
        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) ObjectUtils.getGenericClass(this.getClass());

        // Validate configurations of entity
        if (!ObjectUtils.hasAnnotation(entityClass, CreateRequestClassDTO.class)) {
            throw new InvalidEntityException(entityClass.getSimpleName() + " don't have CreateRequestClassDTO configuration");
        }
        if (!ObjectUtils.hasAnnotation(entityClass, CreateResponseClassDTO.class)) {
            throw new InvalidEntityException(entityClass.getSimpleName() + " don't have CreateResponseClassDTO configuration");
        }

        // Get class of DTO
        Class<?> dtoRequestClass = ObjectUtils.getAnnotation(entityClass, CreateRequestClassDTO.class).value();
        // Checking DTO object is have valid Class
        if (!dtoRequestClass.isAssignableFrom(entityDTO.getClass())) {
            throw new InvalidEntityException("Cannot cast " + entityDTO.getClass().getSimpleName() + " to " + entityClass.getSimpleName());
        }

        // Convert DTO to Entity
        T entity = ConvertUtils.convertDTOToEntity(entityDTO, entityClass);

        // Save entity
        entity = save(entity);

        // Convert entity to Response DTO
        Class<?> dtoResponseClass = ObjectUtils.getAnnotation(entityClass, CreateResponseClassDTO.class).value();
        return ConvertUtils.convertEntityToDTO(entity, dtoResponseClass);
    }

    /**
     * Get entity by id.
     *
     * @param id   the id of entity
     * @param <ID> generic of id
     * @return response dto
     */
    protected <ID extends Serializable> Object getEntity(ID id) {
        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) ObjectUtils.getGenericClass(this.getClass());

        // Validate configurations of entity
        if (!ObjectUtils.hasAnnotation(entityClass, ReadResponseClassDTO.class)) {
            throw new InvalidEntityException(entityClass.getSimpleName() + " don't have GetResponseClassDTO configuration");
        }

        // Query entity from database
        T entity = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot found data for entity " + entityClass.getSimpleName()));

        Class<?> dtoResponseClass = ObjectUtils.getAnnotation(entityClass, ReadResponseClassDTO.class).value();
        return ConvertUtils.convertEntityToDTO(entity, dtoResponseClass);
    }

    /**
     * Update entity.
     *
     * @param id        of entity
     * @param entityDTO entity DTO
     * @param <ID>      generic of Id
     * @return updated entity response DTO
     */
    protected <ID extends Serializable> Object updateEntity(ID id, Object entityDTO) {
        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) ObjectUtils.getGenericClass(this.getClass());

        // Validate configurations of entity
        if (!ObjectUtils.hasAnnotation(entityClass, UpdateRequestClassDTO.class)) {
            throw new InvalidEntityException(entityClass.getSimpleName() + " don't have UpdateRequestClassDTO configuration");
        }
        if (!ObjectUtils.hasAnnotation(entityClass, UpdateResponseClassDTO.class)) {
            throw new InvalidEntityException(entityClass.getSimpleName() + " don't have UpdateResponseClassDTO configuration");
        }

        // Get class of DTO
        Class<?> dtoRequestClass = ObjectUtils.getAnnotation(entityClass, UpdateRequestClassDTO.class).value();
        // Checking DTO object is have valid Class
        if (!dtoRequestClass.isAssignableFrom(entityDTO.getClass())) {
            throw new InvalidEntityException("Cannot cast " + entityDTO.getClass().getSimpleName() + " to " + entityClass.getSimpleName());
        }

        // Find entity by id.
        T entity = findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot found data for entity " + entityClass.getSimpleName()));

        // Copy value from DTO to Entity
        ConvertUtils.copyValueFromDTOToEntity(entityDTO, entity);

        // Update entity
        entity = save(entity);

        // Convert entity to Response DTO
        Class<?> dtoResponseClass = ObjectUtils.getAnnotation(entityClass, UpdateResponseClassDTO.class).value();
        return ConvertUtils.convertEntityToDTO(entity, dtoResponseClass);
    }

    /**
     * Delete entity.
     *
     * @param id   id of entity
     * @param <ID> generic of Id
     * @return true if entity is deleted successfully
     */
    protected <ID extends Serializable> boolean deleteEntity(ID id) {
        if (null != id) {
            deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Save entity to database.
     *
     * @param entity entity data
     * @return the entity is created successfully
     */
    @Transactional
    protected T save(T entity) {
        if (null != entity) {
            return repository.save(entity);
        }
        throw new SaveEntityException(ObjectUtils.getGenericClass(this.getClass()) + " is empty, cannot save it");
    }

    /**
     * Find entity by Id.
     *
     * @param id   the id of entity
     * @param <ID> Generic of Id
     * @return entity if it's exist
     */
    @Transactional(readOnly = true)
    protected <ID extends Serializable> Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    /**
     * Delete entity.
     *
     * @param entity entity need to delete
     */
    @Transactional
    protected void delete(T entity) {
        if (null != entity) {
            repository.delete(entity);
        }
    }

    /**
     * Delete entity by id.
     *
     * @param id   id of entity
     * @param <ID> generic of id, it must be extends from Serializable
     */
    @Transactional
    protected <ID extends Serializable> void deleteById(ID id) {
        if (null != id) {
            repository.deleteById(id);
        }
    }

    /**
     * Set repository.
     *
     * @param repository repository
     */
    @Autowired
    public void setRepository(DefaultHttpRepository<T> repository) {
        this.repository = repository;
    }
}