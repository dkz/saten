package su.dkzde.saten;

import su.dkzde.Iterators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitry Kozlov
 */
public final class IOStorageReplicator implements ReadWriteStorage {

    private final ReadWriteStorage underlying;
    private final IOWritableStorage replica;

    public static IOStorageReplicator create(ReadWriteStorage underlying, IOWritableStorage replica) {
        return new IOStorageReplicator(underlying, replica);
    }

    private IOStorageReplicator(ReadWriteStorage underlying, IOWritableStorage replica) {
        this.underlying = underlying;
        this.replica = replica;
    }


    @Override
    public <C extends Serializable> void instancePut(StorageKey<C> key, C instance) throws StorageException {
        replica.instancePut(key, instance);
        underlying.instancePut(key, instance);

    }

    @Override
    public <C extends Serializable> void listAppend(StorageKey<C> key, Iterator<C> iterator) throws StorageException {
        ArrayList<C> list = Iterators.collect(iterator);
        replica.listAppend(key, list.iterator());
        underlying.listAppend(key, list.iterator());
    }

    @Override
    public <C extends Serializable> void listInsert(StorageKey<C> key, Iterator<C> iterator, int index) throws StorageException {
        ArrayList<C> list = Iterators.collect(iterator);
        replica.listInsert(key, list.iterator(), index);
        underlying.listInsert(key, list.iterator(), index);
    }

    @Override
    public <C extends Serializable> void listRemove(StorageKey<C> key, Iterator<C> iterator) throws StorageException {
        ArrayList<C> list = Iterators.collect(iterator);
        replica.listRemove(key, list.iterator());
        underlying.listRemove(key, list.iterator());
    }

    @Override
    public <C extends Serializable> void setAppend(StorageKey<C> key, Iterator<C> iterator) throws StorageException {
        ArrayList<C> list = Iterators.collect(iterator);
        replica.setAppend(key, list.iterator());
        underlying.setAppend(key, list.iterator());
    }

    @Override
    public <C extends Serializable> void setRemove(StorageKey<C> key, Iterator<C> iterator) throws StorageException {
        ArrayList<C> list = Iterators.collect(iterator);
        replica.setRemove(key, list.iterator());
        underlying.setRemove(key, list.iterator());
    }

    @Override
    public <C extends Serializable> void remove(StorageKey<C> key) throws StorageException {
        replica.remove(key);
        underlying.remove(key);
    }

    @Override
    public <C extends Serializable> C instanceGet(StorageKey<C> key) {
        return underlying.instanceGet(key);
    }

    @Override
    public <C extends Serializable> List<C> listGet(StorageKey<C> key) {
        return underlying.listGet(key);
    }

    @Override
    public <C extends Serializable> Set<C> setGet(StorageKey<C> key) {
        return underlying.setGet(key);
    }
}
