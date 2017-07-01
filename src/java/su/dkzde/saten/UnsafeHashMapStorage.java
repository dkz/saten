package su.dkzde.saten;

import su.dkzde.Iterators;

import java.io.Serializable;
import java.util.*;

/**
 * Consists of single {@code HashMap} which is used as a storage for instances.
 * @author Dmitry Kozlov
 */
public final class UnsafeHashMapStorage implements WritableStorage, ReadableStorage, Serializable {

    private final Map<StorageKey, Serializable> store;

    private UnsafeHashMapStorage() {
        store = new HashMap<>();
    }

    private UnsafeHashMapStorage(Map<StorageKey, Serializable> map) {
        store = map;
    }

    public static UnsafeHashMapStorage create() {
        return new UnsafeHashMapStorage();
    }

    public static UnsafeHashMapStorage copyOf(UnsafeHashMapStorage storage) {
        return new UnsafeHashMapStorage(new HashMap<>(storage.store));
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof UnsafeHashMapStorage) {
            UnsafeHashMapStorage that = (UnsafeHashMapStorage) object;
            return Objects.equals(this.store, that.store);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return store.hashCode();
    }

    @Override
    public <C extends Serializable> C instanceGet(StorageKey<C> key) {
        return key.type().cast(store.get(key));
    }

    @SuppressWarnings("unchecked")
    @Override public <C extends Serializable> List<C> listGet(StorageKey<C> key) {
        Serializable instance = store.computeIfAbsent(key, k -> new ArrayList());
        return (List<C>) instance;
    }

    @SuppressWarnings("unchecked")
    @Override public <C extends Serializable> Set<C> setGet(StorageKey<C> key) {
        Serializable instance = store.computeIfAbsent(key, k -> new HashSet());
        return (Set<C>) instance;
    }

    @Override
    public <C extends Serializable> void instancePut(StorageKey<C> key, C instance) {
        store.put(key, instance);
    }

    @SuppressWarnings("unchecked")
    @Override public <C extends Serializable> void listAppend(StorageKey<C> key, Iterator<C> iterator) {
        Serializable instance = store.computeIfAbsent(key, k -> new ArrayList());
        List list = (List) instance;
        list.addAll(Iterators.collect(iterator));
    }

    @SuppressWarnings("unchecked")
    @Override public <C extends Serializable> void listInsert(StorageKey<C> key, Iterator<C> iterator, int index) {
        Serializable instance = store.computeIfAbsent(key, k -> new ArrayList());
        List list = (List) instance;
        list.addAll(index, Iterators.collect(iterator));
    }

    @SuppressWarnings("unchecked")
    @Override public <C extends Serializable> void listRemove(StorageKey<C> key, Iterator<C> iterator) {
        Serializable instance = store.computeIfAbsent(key, k -> new ArrayList());
        List list = (List) instance;
        list.removeAll(Iterators.collect(iterator));
    }

    @SuppressWarnings("unchecked")
    @Override public <C extends Serializable> void setAppend(StorageKey<C> key, Iterator<C> iterator) {
        Serializable instance = store.computeIfAbsent(key, k -> new HashSet());
        Set set = (Set) instance;
        set.addAll(Iterators.collect(iterator));
    }

    @SuppressWarnings("unchecked")
    @Override public <C extends Serializable> void setRemove(StorageKey<C> key, Iterator<C> iterator) {
        Serializable instance = store.computeIfAbsent(key, k -> new HashSet());
        Set set = (Set) instance;
        set.removeAll(Iterators.collect(iterator));
    }

    @Override
    public <C extends Serializable> void remove(StorageKey<C> key) throws StorageException {
        store.remove(key);
    }
}
