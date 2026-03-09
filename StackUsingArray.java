public class StackUsingArray {
    private int maxSize;   // maximum capacity of stack
    private int[] stackArray; // array to store elements
    private int top;       // index of top element

    // Constructor
    public StackUsingArray(int size) {
        maxSize = size;
        stackArray = new it[maxSize];
        top = -1; // stack is initially empty
    }

    // Push operation: add element to the top
    public void push(int value) {
        if (isFull()) {
            System.out.println("Stack Overflow! Cannot push " + value);
            return;
        }
        top = top +1;
        stackArray[top] = value;
        System.out.println(value + " pushed to stack.");
    }

    // Pop operation: remove top element
    public int pop() {
        if (isEmpty()) {
            System.out.println("Stack Underflow! Cannot pop.");
            return -1; // or throw exception
        }
        return stackArray[top--];
    }

    // Peek operation: view top element
    public int peek() {
        if (isEmpty()) {
            System.out.println("Stack is empty.");
            return -1;
        }
        return stackArray[top];
    }

    // Check if stack is empty
    public boolean isEmpty() {
        return (top == -1);
    }

    // Check if stack is full
    public boolean isFull() {
        return (top == maxSize - 1);
    }

    // Display elements
    public void display() {
        if (isEmpty()) {
            System.out.println("Stack is empty.");
            return;
        }
        System.out.print("Stack elements: ");
        for (int i = 0; i <= top; i++) {
            System.out.print(stackArray[i] + " ");
        }
        System.out.println();
    }

    // Main method to test the stack
    public static void main(String[] args) {
        StackUsingArray stack = new StackUsingArray(5);

        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.display();

        System.out.println("Top element: " + stack.peek());
        System.out.println("Popped element: " + stack.pop());
        stack.display();
    }
}
