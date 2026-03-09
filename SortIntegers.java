public class SortIntegers {

    // -------------------------
    // Bubble Sort
    // -------------------------
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // -------------------------
    // Selection Sort
    // -------------------------
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    // -------------------------
    // Insertion Sort
    // -------------------------
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // -------------------------
    // Utility functions
    // -------------------------
    public static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    public static int[] copyArray(int[] arr) {
        int[] copy = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }

    // -------------------------
    // Main method
    // -------------------------
    public static void main(String[] args) {
        // Hardcoded array of integers
        int[] numbers = { 45, 12, 78, 34, 23, 89, 10 };

        System.out.println("Original array:");
        printArray(numbers);

        // Bubble Sort
        int[] bubbleSorted = copyArray(numbers);
        bubbleSort(bubbleSorted);
        System.out.println("\nBubble Sort:");
        printArray(bubbleSorted);

        // Selection Sort
        int[] selectionSorted = copyArray(numbers);
        selectionSort(selectionSorted);
        System.out.println("\nSelection Sort:");
        printArray(selectionSorted);

        // Insertion Sort
        int[] insertionSorted = copyArray(numbers);
        insertionSort(insertionSorted);
        System.out.println("\nInsertion Sort:");
        printArray(insertionSorted);
    }
}
