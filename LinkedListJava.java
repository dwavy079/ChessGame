 'public class LinkedListJava {

    // Node class (inner class)
    private class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;   // head of the list
    private int size;    // track size

    // Constructor
    public LinkedListJava() {
        head = null;
        size = 0;
    }

    // Insert at beginning
    public void insertAtHead(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
        size++;
    }

    // Insert at end
    public void insertAtTail(int data) {
        Node newNode = new Node(data);
        size++;

        if (head == null) {
            head = newNode;
            return;
        }

        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }

    // Delete first occurrence of a value
    public void delete(int data) {
        if (head == null) return;

        // If deleting head
        if (head.data == data) {
            head = head.next;
            size--;
            return;
        }

        Node current = head;
        while (current.next != null && current.next.data != data) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
            size--;
        }
    }

    // Search for a value
    public boolean search(int data) {
        Node current = head;

        while (current != null) {
            if (current.data == data) return true;
            current = current.next;
        }

        return false;
    }

    // Get size
    public int size() {
        return size;
    }

    // Print list
    public void printList() {
        Node current = head;

        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
        System.out.println("null");
    }

    // Testing
    public static void main(String[] args) {
        LinkedListJava list = new LinkedListJava();

        list.insertAtHead(3);
        list.insertAtHead(2);
        list.insertAtTail(4);
        list.insertAtTail(5);

        list.printList(); // 2 -> 3 -> 4 -> 5 -> null

        System.out.println("Search 3: " + list.search(3)); // true

        list.delete(3);
        list.printList(); // 2 -> 4 -> 5 -> null

        System.out.println("Size: " + list.size());
    }
}
