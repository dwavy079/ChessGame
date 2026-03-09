import java.util.PriorityQueue;

public class PriorityQueueExample {
    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        pq.offer(50);
        pq.offer(10);
        pq.offer(30);
        pq.offer(20);

        System.out.println("Priority Queue: " + pq);

        // Poll elements — returns smallest first
        while (!pq.isEmpty()) {
            System.out.println("Polled: " + pq.poll());
        }
    }
}
