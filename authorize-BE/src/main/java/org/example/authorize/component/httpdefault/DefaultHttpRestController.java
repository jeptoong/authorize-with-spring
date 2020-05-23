package org.example.authorize.component.httpdefault;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.response.WResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedHashMap;

/**
 * Default Rest Controller.
 *
 * @param <T> Generic of entity
 */
@Slf4j
public class DefaultHttpRestController<T extends Audit<?>> {

    private DefaultHttpService<T> service;

    /**
     * Create new entity.
     *
     * @param entity the data of entity
     * @return DTO of entity
     */
    @PostMapping
    public WResponseEntity<Object> create(@RequestBody LinkedHashMap<String, ?> entity) {
        return WResponseEntity.success(service.createAndSaveEntity(entity));
    }

    /**
     * API get.
     *
     * @param id id of entity.
     * @return DTO of entity
     */
    @GetMapping("/{id}")
    public WResponseEntity<String> get(@PathVariable String id) {
        return WResponseEntity.success(id + " ok");
    }

    /**
     * Set Service.
     *
     * @param service service
     */
    @Autowired
    public void setService(DefaultHttpService<T> service) {
        this.service = service;
    }
}
