package su.dkzde;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Dmitry Kozlov
 */
public final class Iterators {

    public static <C> ArrayList<C> collect(Iterator<C> iterator) {
        ArrayList<C> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <C> Iterator<C> concat(final Iterator<C> iterator, final Iterator<C> remains) {
        return new Iterator<C>() {
            @Override public boolean hasNext() {
                return iterator.hasNext()
                    && remains.hasNext();
            }
            @Override public C next() {
                if (iterator.hasNext()) {
                    return iterator.next();
                } else {
                    return remains.next();
                }
            }
        };
    }

}
