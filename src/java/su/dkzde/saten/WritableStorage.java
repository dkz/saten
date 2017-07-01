package su.dkzde.saten;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author Dmitry Kozlov
 */
public interface WritableStorage {

    <C extends Serializable> void instancePut(StorageKey<C> key, C instance) throws StorageException;

    <C extends Serializable> void listAppend(StorageKey<C> key, Iterator<C> iterator) throws StorageException;

    <C extends Serializable> void listInsert(StorageKey<C> key, Iterator<C> iterator, int index) throws StorageException;

    <C extends Serializable> void listRemove(StorageKey<C> key, Iterator<C> iterator) throws StorageException;

    <C extends Serializable> void setAppend(StorageKey<C> key, Iterator<C> iterator) throws StorageException;

    <C extends Serializable> void setRemove(StorageKey<C> key, Iterator<C> iterator) throws StorageException;

    <C extends Serializable> void remove(StorageKey<C> key) throws StorageException;
}
