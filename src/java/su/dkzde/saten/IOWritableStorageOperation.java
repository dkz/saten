package su.dkzde.saten;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Dmitry Kozlov
 */
public abstract class IOWritableStorageOperation implements Serializable {

    private final Date ts;

    public interface ActionHandler {
        void initializeStorage(UnsafeHashMapStorage storage);
        void applyOperation(StorageOperation operation);
    }

    protected IOWritableStorageOperation(Date ts) {
        this.ts = ts;
    }

    public Date getTimestamp() {
        return ts;
    }

    abstract public void apply(ActionHandler handler);

    public static IOWritableStorageOperation initialState(UnsafeHashMapStorage storage, Date ts) {
        return new InitialStateWrapper(ts, storage);
    }

    public static IOWritableStorageOperation operation(StorageOperation operation, Date ts) {
        return new OperationWrapper(ts, operation);
    }

    private final static class InitialStateWrapper extends IOWritableStorageOperation implements Serializable {
        private final UnsafeHashMapStorage state;
        private InitialStateWrapper(Date ts, UnsafeHashMapStorage state) {
            super(ts);
            this.state = state;
        }

        @Override public void apply(ActionHandler handler) {
            handler.initializeStorage(state);
        }
    }

    private final static class OperationWrapper extends IOWritableStorageOperation implements Serializable {
        private final StorageOperation wrapped;
        private OperationWrapper(Date ts, StorageOperation wrapped) {
            super(ts);
            this.wrapped = wrapped;
        }

        @Override public void apply(ActionHandler handler) {
            handler.applyOperation(wrapped);
        }
    }
}
