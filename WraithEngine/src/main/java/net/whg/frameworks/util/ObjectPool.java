package net.whg.frameworks.util;

import java.util.LinkedList;

/**
 * A thread-safe utility class that represents a pool of objects which are
 * poolable
 * 
 * @param <T> the type of the objects in the pool
 */
public abstract class ObjectPool<T extends Poolable> {
    private LinkedList<T> _pool = new LinkedList<>();
    

    /**
     * @return the first element of the pool if its non-empty otherwise return a newly built element 
     */
    public T get() {
        T t;
        synchronized (_pool) {
            if (_pool.isEmpty())
                return build();

            t = _pool.removeFirst();
        }

        t.init();
        return t;
    }

    /**
     * Puts an element in the pool.
     * @param t
     *        - The element to put in the pool
     */
    public void put(T t) {
        t.dispose();

        synchronized (_pool) {
            _pool.addLast(t);
        }
    }
    
    /**
     * @return an instance of a T object
     */
    protected abstract T build();
}
