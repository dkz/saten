package su.dkzde.saten;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Dmitry Kozlov
 */
public interface ReadableStorage {

    <C extends Serializable> C instanceGet(StorageKey<C> key);

    <C extends Serializable> List<C> listGet(StorageKey<C> key);

    <C extends Serializable> Set<C> setGet(StorageKey<C> key);
}
