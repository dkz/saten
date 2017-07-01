package su.dkzde.saten;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Date;

/**
 * @author Dmitry Kozlov
 */
public final class IOStorageSnapshotReader {

    private IOStorageSnapshotReader() {}

    private static class SnapshotContext {
        UnsafeHashMapStorage storage = UnsafeHashMapStorage.create();
    }

    public static UnsafeHashMapStorage readSnapshot(Date ts, InputStream stream) throws IOException {
        final SnapshotContext context = new SnapshotContext();
        try {
            while (true) {
                IOWritableStorageOperation operation = readOperation(stream);
                if (ts.before(operation.getTimestamp())) {
                    return context.storage;
                } else {
                    operation.apply(new IOWritableStorageOperation.ActionHandler() {
                        @Override
                        public void initializeStorage(UnsafeHashMapStorage storage) {
                            context.storage = storage;
                        }
                        @Override
                        public void applyOperation(StorageOperation operation) {
                            operation.apply(context.storage);
                        }
                    });
                }
            }
        } catch (EOFException exception) {
            return context.storage;
        }
    }

    public static IOWritableStorageOperation readOperation(InputStream stream) throws IOException {
        ObjectInputStream in = new ObjectInputStream(stream);
        try {
            return (IOWritableStorageOperation) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
