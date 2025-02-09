import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;

    // Construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[2];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, temp, 0, size);
        items = temp;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == items.length) resize(2 * items.length);
        items[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniform(size);
        Item item = items[index];
        items[index] = items[--size];
        items[size] = null; // Avoid loitering
        if (size > 0 && size == items.length / 4) resize(items.length / 2);
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return items[StdRandom.uniform(size)];
    }

    public Iterator<Item> iterator() {
        return new Iterator<>() {
            private final Item[] shuffled;
            private int current = 0;

            {
                shuffled = (Item[]) new Object[size];
                System.arraycopy(items, 0, shuffled, 0, size);
                StdRandom.shuffle(shuffled);
            }

            public boolean hasNext() {
                return current < shuffled.length;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                return shuffled[current++];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // Unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        System.out.println("Sample: " + rq.sample());
        System.out.println("Dequeue: " + rq.dequeue());
        System.out.println("Size: " + rq.size());
    }
}
