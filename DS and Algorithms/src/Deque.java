import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size;

    private class Node {
        Item item;
        Node next, prev;
    }

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        if (isEmpty()) last = first;
        else oldFirst.prev = first;
        size++;
    }

    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.prev = oldLast;
        if (isEmpty()) first = last;
        else oldLast.next = last;
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        if (first == null) last = null;
        else first.prev = null;
        size--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = last.item;
        last = last.prev;
        if (last == null) first = null;
        else last.next = null;
        size--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new Iterator<>() {
            private Node current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                Item item = current.item;
                current = current.next;
                return item;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // Unit testing
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(0);
        System.out.println("Size: " + deque.size()); // 3
        System.out.println("Remove First: " + deque.removeFirst()); // 0
        System.out.println("Remove Last: " + deque.removeLast()); // 2
    }
}
