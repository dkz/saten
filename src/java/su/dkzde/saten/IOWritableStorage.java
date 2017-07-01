package su.dkzde.saten;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import su.dkzde.TimeProvider;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Dmitry Kozlov
 */
public final class IOWritableStorage implements WritableStorage {

    private final TimeProvider time;
    private final WritableStorage underlying;

    private final OutputStream stream;
    private final LinkedList<byte[]> dataQueue = new LinkedList<>();
    private long sizeBytes = 0;

    public static IOWritableStorage create(TimeProvider time, OutputStream stream) {
        return new IOWritableStorage(time, stream);
    }

    private IOWritableStorage(TimeProvider time, OutputStream stream) {
        this.time = time;
        this.stream = stream;
        this.underlying = OperationConsumerStorage.create(this::store);
    }

    public long getSizeInBytes() {
        return sizeBytes;
    }

    private void store(StorageOperation operation) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ObjectOutputStream serializer = new ObjectOutputStream(out)) {
            serializer.writeUnshared(IOWritableStorageOperation.operation(operation, time.current()));
        } catch (IOException exception) {
            throw new StorageException(exception);
        }
        store(out.toByteArray());
    }

    private void store(byte[] bytes) {
        dataQueue.addLast(bytes);
        while (!dataQueue.isEmpty()) {
            byte[] data = dataQueue.poll();
            try {
                stream.write(data);
                stream.flush();
                sizeBytes += data.length;
            } catch (IOException exception) {
                dataQueue.addFirst(data);
                throw new StorageException(exception);
            }
        }
    }

    @Override
    public <C extends Serializable> void instancePut(StorageKey<C> key, C instance) {
        underlying.instancePut(key, instance);
    }

    @Override
    public <C extends Serializable> void listAppend(StorageKey<C> key, Iterator<C> iterator) {
        underlying.listAppend(key, iterator);
    }

    @Override
    public <C extends Serializable> void listInsert(StorageKey<C> key, Iterator<C> iterator, int index) {
        underlying.listInsert(key, iterator, index);
    }

    @Override
    public <C extends Serializable> void listRemove(StorageKey<C> key, Iterator<C> iterator) {
        underlying.listRemove(key, iterator);
    }

    @Override
    public <C extends Serializable> void setAppend(StorageKey<C> key, Iterator<C> iterator) {
        underlying.setAppend(key, iterator);
    }

    @Override
    public <C extends Serializable> void setRemove(StorageKey<C> key, Iterator<C> iterator) {
        underlying.setRemove(key, iterator);
    }

    @Override
    public <C extends Serializable> void remove(StorageKey<C> key) throws StorageException {
        underlying.remove(key);
    }
}
