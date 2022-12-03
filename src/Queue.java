public class Queue<E> {
    private Node<E> head;
    private Node<E> tail;
    private int length;
    private static int numberEnqueues = 0;

    public void resetEnqueues() {
        numberEnqueues = 0;
    }
    public int getEnqueues() {
        return numberEnqueues;
    }
    public E getHead() {
        return head.value;
    }
    public int getLength() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public void enqueue(E value) {
        if (head == null) {
            head = new Node<>(value);
            tail = head;
        }
        else {
            tail.next = new Node<>(value);
            tail = tail.next;
        }
        numberEnqueues++;
        length++;
    }

    public E dequeue() {
        if (head != null) {
            Node<E> oldHead = head;
            head = head.next;
            length--;
            return oldHead.value;
        }
        return null;
    }

    public void print(int numberEntries) {
        Node<E> currentEntry = head;
        for (int i = 0; i < numberEntries; i++) {
            if (currentEntry == null) {
                break;
            }
            System.out.print(currentEntry.value);
            currentEntry = currentEntry.next;
        }
    }

    private class Node<T> {
        public T value;
        public Node<T> next;

        public Node(T value) {
            this.value = value;
        }
    }
}
