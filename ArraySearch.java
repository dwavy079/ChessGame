public class ArraySearch {
    
    // Linear search method
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i; // Found at index i
            }
        }
        return -1; // Not found
    }

    // Binary search - only if the array is sorted!
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // index of the mid element

            if (arr[mid] == target) {
                return mid; // element found
            } else if (arr[mid] < target) {
                left = mid + 1; // search in the right half
            } else {
                right = mid - 1; // search in the left half
            }
        }
        return -1; // element not found
    }



    public static void main(String[] args) {
        
        int n = 100;  //size of the array
        
        int[] numbers = new int[n];  //create the empty array

        //populate the array with incremental numbers
        for (int i=0; i<n; i++)
        {
            numbers[i] = i;  
        }

        //number to be searched
        int target = 21;

        //linear search
        int index_linear = linearSearch(numbers, target);
        System.out.println("Output of linear search: "+index_linear);

        //linear search
        int index_binary = binarySearch(numbers, target);
        System.out.println("Output of binary search: "+index_binary);

    }
}
