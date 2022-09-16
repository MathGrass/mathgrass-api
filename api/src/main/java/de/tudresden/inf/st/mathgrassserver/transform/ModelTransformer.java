package de.tudresden.inf.st.mathgrassserver.transform;

import java.util.ArrayList;
import java.util.List;

public abstract class ModelTransformer<D,E> {

    public ModelTransformer() {

    }

    Object role;

    Object getUsedRole() {
        return this.role;
    }

    public ModelTransformer(Object role) {
        this.role = role;
    }

    public abstract D toDto(E entity);
    public abstract E toEntity(D dto);

    public List<D> toDtoList(List<E> entityList) {
        ArrayList<D> dtos = new ArrayList<>();
        for (E e : entityList) {
            dtos.add(toDto(e));
        }
        return dtos;
    }

    public List<E> toEntityList(List<D> dtoList) {
        ArrayList<E> entities = new ArrayList<>();
        for (D d : dtoList) {
            entities.add(toEntity(d));
        }
        return entities;
    }
}
