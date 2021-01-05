package services.metrics;

public class CircularQueue<T> {

    final int capacity;

    int head = 0;

    T[] values ;

    public CircularQueue(int capacity, T unusedValue)
    {
        this.capacity = capacity;
        values  = (T[]) new Object[capacity];

        for (int i=0;i<capacity;i++)
            values[i] = unusedValue;
    }

    public void add(T t)
    {
        values[head%capacity] = t;
        head++;
    }

    public T[] get()
    {
        T[] valuesToReturn = (T[])new Object[capacity];
        int index=0;
        int pos = (head-1)%capacity;
        for (int i=pos; i>=0;i--)
        {
            valuesToReturn[index++] = values[i];
        }

        for (int i=capacity-1;i>pos;i--)
        {
            valuesToReturn[index++] = values[i];

        }

        return valuesToReturn;
    }


}
