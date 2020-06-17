package ru.complitex.common.model;

import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableSupplier;

/**
 * @author Anatoly Ivanov
 * 17.06.2020 18:08
 */
public abstract class LoadableModel<T> implements IModel<T> {
    private T object;

    protected abstract T load();

    @Override
    public T getObject() {
        if (object == null){
            object = load();
        }

        return object;
    }

    @Override
    public void setObject(T object) {
        this.object = object;
    }

    public static <T> LoadableModel<T> of(SerializableSupplier<T> getter){
        return new LoadableModel<T>() {
            @Override
            protected T load() {
                return getter.get();
            }
        };
    }
}
