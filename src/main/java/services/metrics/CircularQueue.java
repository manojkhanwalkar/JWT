/*
 * CircularQueue
 *
 * Fixed-capacity ring buffer that keeps the last N values added. When it is full, the
 * oldest value is overwritten. Meant to hold the most recent metric samples for each
 * metric. Not thread-safe.
 */
package services.metrics;

public class CircularQueue<T> {

    final int capacity;

    /** Index where the next value is written. Always in [0, capacity), so it never overflows. */
    private int head = 0;

    private final Object[] values;

    /**
     * @param capacity    number of values kept
     * @param unusedValue placeholder in slots that have not been written yet
     */
    public CircularQueue(int capacity, T unusedValue) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        values = new Object[capacity];
        java.util.Arrays.fill(values, unusedValue);
    }

    /** Adds a value. If the buffer is full, the oldest value is overwritten. */
    public void add(T t) {
        values[head] = t;
        head = (head + 1) % capacity;
    }

    /**
     * Returns a snapshot of all {@code capacity} slots, newest first. Slots that have
     * never been written hold the placeholder value.
     */
    @SuppressWarnings("unchecked")
    public T[] get() {
        Object[] result = new Object[capacity];
        int pos = (head - 1 + capacity) % capacity;
        for (int i = 0; i < capacity; i++) {
            result[i] = values[(pos - i + capacity) % capacity];
        }
        return (T[]) result;
    }
}
