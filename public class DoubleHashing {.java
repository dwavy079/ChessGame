public class DoubleHashing {

    private int[] table;
    private int size;

    public DoubleHashing(int size) {
        this.size = size;
        table = new int[size];
        for (int i = 0; i < size; i++) {
            table[i] = -1;  // -1 indicates an empty slot
        }
    }

    // Primary hash function
    private int hash1(int key) {
        return key % size;
    }

    // Secondary hash function
    private int hash2(int key) {
        return 1 + (key % (size - 1));  // Avoids 0
    }

    // Insert value into the hash table using double hashing
    public void insert(int key) {
        int index = hash1(key);
        int stepSize = hash2(key);
        int i = 0;
        while (table[(index + i * stepSize) % size] != -1) {  // Double hashing
            i++;
            if (i == size) {
                System.out.println("Table is full.");
                return;
            }
        }
        table[(index + i * stepSize) % size] = key;
    }

    // Print the hash table
    public void printTable() {
        for (int i = 0; i < size; i++) {
            if (table[i] == -1) {
                System.out.print("empty ");
            } else {
                System.out.print(table[i] + " ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        DoubleHashing dh = new DoubleHashing(13);

        // Insert keys into the table (simulating the given data)
        dh.insert(261);
        dh.insert(40);
        dh.insert(560);
        dh.insert(900);
        dh.insert(822);
        dh.insert(29);
        dh.insert(265);
        dh.insert(53);
        dh.insert(921);

        // Print the table after insertion
        dh.printTable();
    }
}
