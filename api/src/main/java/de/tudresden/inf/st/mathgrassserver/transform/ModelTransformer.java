package de.tudresden.inf.st.mathgrassserver.transform;

public abstract class ModelTransformer<D,E> {
    public abstract D toDto(E entity);
    public abstract E toEntity(D dto);
}
