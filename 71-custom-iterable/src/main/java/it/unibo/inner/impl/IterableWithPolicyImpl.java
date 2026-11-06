package it.unibo.inner.impl;

import java.util.Iterator;
import java.util.NoSuchElementException;

import it.unibo.inner.api.*;

/**    (non-Javadoc)
 * 
 * {@inheritDoc}
 * @see it.unibo.inner.api.IterableWithPolicy#setIterationPolicy(it.unibo.inner.api.Predicate)
 */
public class IterableWithPolicyImpl<T> implements IterableWithPolicy<T> {

    private final T[] array;
    private Predicate<T> filter;

    public IterableWithPolicyImpl(final T[] array, final Predicate<T> filter) {
        this.array = array;
        this.filter = filter;
    }

    public IterableWithPolicyImpl(final T[] array) {
        this(
            array,
            new Predicate<>() {
                public boolean test(final T elem) {
                    return true;
                }
            }
        );
    }

    @Override
    public void setIterationPolicy(final Predicate<T> filter) {
        this.filter = filter;
    }

    @Override
    public Iterator<T> iterator() {
        return new PolicyIterator();
    }

    private class PolicyIterator implements Iterator<T> {

        private int currentIndex = 0;

        private int checkNext(final int index) {
            if (index >= array.length) {
                throw new NoSuchElementException(
                    "There's no compatible element: currentIndex=" + currentIndex
                );
            } else if (filter.test(array[index])) {
                return index;
            }
            return checkNext(index + 1);
        }
        
        @Override
        public boolean hasNext() {
            // return currentIndex < array.length;
            try {
                checkNext(currentIndex);
            } catch (final NoSuchElementException exception) {
                return false;
            }
            return true;
        }

        @Override
        public T next() {
            // return array[currentIndex++];
            try {
                currentIndex = checkNext(currentIndex);
            } catch (final NoSuchElementException exception) {
                return null;
            }
            return array[currentIndex++];
        }
    }
}
