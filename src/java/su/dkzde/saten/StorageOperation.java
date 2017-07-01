package su.dkzde.saten;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Dmitry Kozlov
 */
public abstract class StorageOperation implements Serializable {

    protected final StorageKey key;
    protected StorageOperation(StorageKey<?> key) {
        this.key = key;
    }

    abstract public void apply(WritableStorage storage);

    public static <C extends Serializable> StorageOperation instancePut(StorageKey<C> key, C instance) {
        return new InstancePut(key, instance);
    }

    public static <C extends Serializable> StorageOperation listAppend(StorageKey<C> key, ArrayList<C> instances) {
        return new ListAppend(key, instances);
    }

    public static <C extends Serializable> StorageOperation listInsert(StorageKey<C> key, ArrayList<C> instances, int index) {
        return new ListInsert(key, instances, index);
    }

    public static <C extends Serializable> StorageOperation listRemove(StorageKey<C> key, ArrayList<C> instances) {
        return new ListRemove(key, instances);
    }

    public static <C extends Serializable> StorageOperation setAppend(StorageKey<C> key, ArrayList<C> instances) {
        return new SetAppend(key, instances);
    }

    public static <C extends Serializable> StorageOperation setRemove(StorageKey<C> key, ArrayList<C> instances) {
        return new SetRemove(key, instances);
    }

    public static <C extends Serializable> StorageOperation remove(StorageKey<C> key) {
        return new Remove(key);
    }

    private static final class InstancePut extends StorageOperation implements Serializable {
        private final Serializable instance;
        protected <C extends Serializable> InstancePut(StorageKey<C> key, C instance) {
            super(key);
            this.instance = instance;
        }
        @SuppressWarnings("unchecked")
        @Override public void apply(WritableStorage storage) {
            storage.instancePut(key, instance);
        }
    }

    private static final class ListAppend extends StorageOperation implements Serializable {
        private final ArrayList<? extends Serializable> instances;
        protected <C extends Serializable> ListAppend(StorageKey<C> key, ArrayList<C> instances) {
            super(key);
            this.instances = instances;
        }
        @SuppressWarnings("unchecked")
        @Override public void apply(WritableStorage storage) {
            storage.listAppend(key, instances.iterator());
        }
    }

    private static final class ListInsert extends StorageOperation implements Serializable {
        private final ArrayList<? extends Serializable> instances;
        private final int index;
        protected <C extends Serializable> ListInsert(StorageKey<C> key, ArrayList<C> instances, int index) {
            super(key);
            this.instances = instances;
            this.index = index;
        }
        @SuppressWarnings("unchecked")
        @Override public void apply(WritableStorage storage) {
            storage.listInsert(key, instances.iterator(), index);
        }
    }

    private static final class ListRemove extends StorageOperation implements Serializable {
        private final ArrayList<? extends Serializable> instances;
        protected <C extends Serializable> ListRemove(StorageKey<C> key, ArrayList<C> instances) {
            super(key);
            this.instances = instances;
        }
        @SuppressWarnings("unchecked")
        @Override public void apply(WritableStorage storage) {
            storage.listRemove(key, instances.iterator());
        }
    }

    private static final class SetAppend extends StorageOperation implements Serializable {
        private final ArrayList<? extends Serializable> instances;
        protected <C extends Serializable> SetAppend(StorageKey<C> key, ArrayList<C> instances) {
            super(key);
            this.instances = instances;
        }
        @SuppressWarnings("unchecked")
        @Override public void apply(WritableStorage storage) {
            storage.setAppend(key, instances.iterator());
        }
    }

    private static final class SetRemove extends StorageOperation implements Serializable {
        private final ArrayList<? extends Serializable> instances;
        protected <C extends Serializable> SetRemove(StorageKey<C> key, ArrayList<C> instances) {
            super(key);
            this.instances = instances;
        }
        @SuppressWarnings("unchecked")
        @Override public void apply(WritableStorage storage) {
            storage.setRemove(key, instances.iterator());
        }
    }

    private static final class Remove extends StorageOperation implements Serializable {
        protected <C extends Serializable> Remove(StorageKey<C> key) {
            super(key);
        }
        @Override
        public void apply(WritableStorage storage) {
            storage.remove(key);
        }
    }
}
