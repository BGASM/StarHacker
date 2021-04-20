package starhacker.ui.filter;

public interface Filter<T> {

    public boolean accept(T object);
}
