package su.dkzde.saten;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Dmitry Kozlov
 */
public final class StorageKey<C extends Serializable> implements Serializable {

    private final Class<C> type;
    private final Serializable discriminator;
    private final int[] hash;

    public static <C extends Serializable> StorageKey<C> create(Class<C> type, Serializable discriminator) {
        return new StorageKey<>(type, discriminator, new int[] { discriminator.hashCode() });
    }

    public static <C extends Serializable> StorageKey<C> create(Class<C> type, Serializable discriminator, Object... path) {
        int[] hash = new int[path.length];
        for (int j = 0; j < path.length; j++) {
            hash[j] = Objects.hashCode(path[j]);
        }
        return new StorageKey<>(type, discriminator, hash);
    }

    protected StorageKey(Class<C> type, Serializable discriminator, int[] hash) {
        this.type = type;
        this.discriminator = discriminator;
        this.hash = hash;
    }

    public Class<C> type() {
        return type;
    }

    @Override public int hashCode() {
        return Arrays.hashCode(hash);
    }

    @Override public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof StorageKey) {
            StorageKey that = (StorageKey) object;
            return Arrays.equals(this.hash, that.hash)
                && Objects.equals(this.discriminator, that.discriminator);
        }
        return false;
    }
}
