package starhacker.ui;



import starhacker.ui.filter.Filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollectionsHelper {

    public static <E, F extends Filter<E>> void reduce(Iterable<E> entities, Iterable<F> filters) {
        Iterator<E> entity = entities.iterator();
        while (entity.hasNext()) {
            if (!matches(entity.next(), filters)) {
                entity.remove();
            }
        }
    }

    public static <E, F extends Filter<E>> void reduce(Iterable<E> entities, F filter) {
        Iterator<E> entity = entities.iterator();
        while (entity.hasNext()) {
            if (!filter.accept(entity.next())) {
                entity.remove();
            }
        }
    }

    public static <E, F extends Filter<E>> List<E> reduceList(Iterable<E> entities, F filter) {
        Iterator<E> entity = entities.iterator();
        List<E> toReturn = new ArrayList<>();
        while (entity.hasNext()) {
            E ent = entity.next();
            if (filter.accept(ent)) {
                toReturn.add(ent);
            }
        }
        return toReturn;
    }

    private static <E, F extends Filter<E>> boolean matches(E item, Iterable<F> filters) {
        Iterator<F> filter = filters.iterator();
        while (filter.hasNext()) {
            if (!filter.next().accept(item)) {
                return false;
            }
        }
        return true;
    }
}
