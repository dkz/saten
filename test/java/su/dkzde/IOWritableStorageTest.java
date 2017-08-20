package su.dkzde;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import su.dkzde.saten.IOStorageSnapshotReader;
import su.dkzde.saten.IOWritableStorage;
import su.dkzde.saten.StorageKey;
import su.dkzde.saten.UnsafeHashMapStorage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.function.Consumer;

import static java.util.Arrays.asList;

/**
 * @author Dmitry Kozlov
 */
public class IOWritableStorageTest {

    private Date start;
    private Calendar calendar = Calendar.getInstance();
    private TimeProvider time = calendar::getTime;
    private ByteArrayOutputStream out;
    private IOWritableStorage storage;

    @BeforeMethod public void beforeMethod() {
        start = new Date();
        out = new ByteArrayOutputStream();
        storage = IOWritableStorage.create(time, out);
        calendar.setTime(start);
    }

    private void withSnapshotAt(Calendar calendar, Consumer<UnsafeHashMapStorage> consumer) throws IOException {
        consumer.accept(IOStorageSnapshotReader.readSnapshot(calendar.getTime(), new ByteArrayInputStream(out.toByteArray())));
    }

    @Test public void testInstanceOpeationsSnapshotRecovery() throws IOException {
        StorageKey<Integer> one = StorageKey.create(Integer.class, "one");
        StorageKey<Integer> two = StorageKey.create(Integer.class, "two");

        storage.instancePut(one, 1);
        calendar.add(Calendar.MINUTE, 2);
        storage.instancePut(two, 2);
        calendar.add(Calendar.MINUTE, 2);
        storage.remove(one);

        calendar.setTime(start);
        withSnapshotAt(calendar, storage -> {
            Assert.assertEquals(storage.instanceGet(one), (Integer) 1);
            Assert.assertEquals(storage.instanceGet(two), (Integer) null);
        });

        calendar.add(Calendar.MINUTE, 2);
        withSnapshotAt(calendar, storage -> {
            Assert.assertEquals(storage.instanceGet(one), (Integer) 1);
            Assert.assertEquals(storage.instanceGet(two), (Integer) 2);
        });

        calendar.add(Calendar.MINUTE, 2);
        withSnapshotAt(calendar, storage -> {
            Assert.assertEquals(storage.instanceGet(one), (Integer) null);
            Assert.assertEquals(storage.instanceGet(two), (Integer) 2);
        });
    }

    @Test public void testListOperationsSnapshotRecovery() throws IOException {
        StorageKey<Integer> key = StorageKey.create(Integer.class, "list");

        storage.listAppend(key, asList(1, 2).iterator());
        calendar.add(Calendar.MINUTE, 1);
        storage.listAppend(key, asList(3, 4).iterator());
        calendar.add(Calendar.MINUTE, 1);
        storage.listInsert(key, asList(0).iterator(), 0);
        calendar.add(Calendar.MINUTE, 1);
        storage.listRemove(key, asList(2, 3).iterator());

        calendar.setTime(start);
        withSnapshotAt(calendar, storage -> {
            Assert.assertEquals(storage.listGet(key), asList(1, 2));
        });

        calendar.add(Calendar.MINUTE, 1);
        withSnapshotAt(calendar, storage -> {
            Assert.assertEquals(storage.listGet(key), asList(1, 2, 3, 4));
        });
        calendar.add(Calendar.MINUTE, 1);
        withSnapshotAt(calendar, storage -> {
            Assert.assertEquals(storage.listGet(key), asList(0, 1, 2, 3, 4));
        });
        calendar.add(Calendar.MINUTE, 1);
        withSnapshotAt(calendar, storage -> {
            Assert.assertEquals(storage.listGet(key), asList(0, 1, 4));
        });
    }

    @Test public void testSetOperationsSnapshotRecovery() throws IOException {
        StorageKey<Integer> key = StorageKey.create(Integer.class, "set");

        storage.setAppend(key, asList(1, 2).iterator());
        calendar.add(Calendar.MINUTE, 1);
        storage.setAppend(key, asList(3, 4).iterator());
        calendar.add(Calendar.MINUTE, 1);
        storage.setAppend(key, asList(2, 3).iterator());

        calendar.setTime(start);
        withSnapshotAt(calendar, storage -> {
            Assert.assertEquals(storage.setGet(key), new HashSet<>(asList(1, 2)));
        });

        calendar.add(Calendar.MINUTE, 1);
        withSnapshotAt(calendar, storage -> {
            Assert.assertEquals(storage.setGet(key), new HashSet<>(asList(1, 2, 3, 4)));
        });

        calendar.add(Calendar.MINUTE, 1);
        withSnapshotAt(calendar, storage -> {
            Assert.assertEquals(storage.setGet(key), new HashSet<>(asList(1, 4)));
        });
    }

}
