package su.dkzde.saten;

import su.dkzde.Iterators;

import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author Dmitry Kozlov
 */
public final class OperationConsumerStorage implements WritableStorage {

    private final Consumer<StorageOperation> consumer;

    public static OperationConsumerStorage create(Consumer<StorageOperation> consumer) {
        return new OperationConsumerStorage(consumer);
    }

    private OperationConsumerStorage(Consumer<StorageOperation> consumer) {
        this.consumer = consumer;
    }

    @Override
    public <C extends Serializable> void instancePut(StorageKey<C> key, C instance) {
        consumer.accept(StorageOperation.instancePut(key, instance));
    }

    @Override
    public <C extends Serializable> void listAppend(StorageKey<C> key, Iterator<C> iterator) {
        consumer.accept(StorageOperation.listAppend(key, Iterators.collect(iterator)));
    }

    @Override
    public <C extends Serializable> void listInsert(StorageKey<C> key, Iterator<C> iterator, int index) {
        consumer.accept(StorageOperation.listInsert(key, Iterators.collect(iterator), index));
    }

    @Override
    public <C extends Serializable> void listRemove(StorageKey<C> key, Iterator<C> iterator) {
        consumer.accept(StorageOperation.listRemove(key, Iterators.collect(iterator)));
    }

    @Override
    public <C extends Serializable> void setAppend(StorageKey<C> key, Iterator<C> iterator) {
        consumer.accept(StorageOperation.setAppend(key, Iterators.collect(iterator)));
    }

    @Override
    public <C extends Serializable> void setRemove(StorageKey<C> key, Iterator<C> iterator) {
        consumer.accept(StorageOperation.setRemove(key, Iterators.collect(iterator)));
    }

    @Override
    public <C extends Serializable> void remove(StorageKey<C> key) throws StorageException {
        consumer.accept(StorageOperation.remove(key));
    }
}
