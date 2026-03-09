import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Field;

public class HashMapDemo {
    public static void main(String[] args) throws Exception {
        // 1. Create a HashMap with initial capacity 16 and default load factor (0.75)
        HashMap<String, Integer> map = new HashMap<>(16);
        System.out.println("hello".hashCode());
        // 2. Insert key–value pairs
        map.put("apple", 5);
        map.put("banana", 7);
        map.put("cherry", 3);
        map.put("date", 9);

        // 3. Retrieve values using keys
        System.out.println("Value for key 'apple': " + map.get("apple"));
        System.out.println("Value for key 'banana': " + map.get("banana"));
        System.out.println();

        // 4. Check if a key or value exists
        System.out.println("Contains key 'cherry'? " + map.containsKey("cherry"));
        System.out.println("Contains value 10? " + map.containsValue(10));
        System.out.println();

        // 5. Remove an entry
        map.remove("banana");
        System.out.println("After removing 'banana': " + map);
        System.out.println();

        // 6. Iterate over all entries
        System.out.println("Iterating over entries:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        System.out.println();
        System.out.println("Entries:");
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            System.out.println(e.getKey() + " = " + e.getValue() +
                               ", hashCode = " + e.getKey().hashCode());
        }


    }
}
