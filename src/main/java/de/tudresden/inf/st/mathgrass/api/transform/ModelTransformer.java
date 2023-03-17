package de.tudresden.inf.st.mathgrass.api.transform;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class defines the structure of Transformer classes.
 *
 * <p>
 * Transformers can convert database entities to DTOs and vice versa.
 * @param <D> Class of entity
 * @param <E> Entity
 */
public abstract class ModelTransformer<D,E> {

    /**
     * Transform an entity to its respective DTO.
     *
     * @param entity entity
     * @return DTO
     */
    public abstract D toDto(E entity);

    /**
     * Transform a DTO to its respective entity.
     *
     * @param dto DTO
     * @return entity
     */
    public abstract E toEntity(D dto);

    /**
     * Transform a list of entities to their respective DTOs.
     *
     * @param entityList list of entities
     * @return list of DTOs
     */
    public List<D> toDtoList(List<E> entityList) {
        ArrayList<D> dtos = new ArrayList<>();
        for (E e : entityList) {
            dtos.add(toDto(e));
        }

        return dtos;
    }

    /**
     * Transform a list of DTOs to their respective entities.
     *
     * @param dtoList list of DTOs
     * @return list of entities
     */
    public List<E> toEntityList(List<D> dtoList) {
        ArrayList<E> entities = new ArrayList<>();
        for (D d : dtoList) {
            entities.add(toEntity(d));
        }

        return entities;
    }
}
