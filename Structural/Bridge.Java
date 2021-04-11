// Rules of thumb
// Adapter makes things work after they're designed; Bridge makes them work before they are.
// Bridge is designed up-front to let the abstraction and the implementation vary independently. Adapter is retrofitted to make unrelated classes work together.
// State, Strategy, Bridge (and to some degree Adapter) have similar solution structures. They all share elements of the "handle/body" idiom. They differ in intent - that is, they solve different problems.
// The structure of State and Bridge are identical (except that Bridge admits hierarchies of envelope classes, whereas State allows only one). The two patterns use the same structure to solve different problems: State allows an object's behavior to change along with its state, while Bridge's intent is to decouple an abstraction from its implementation so that the two can vary independently.
// If interface classes delegate the creation of their implementation classes (instead of creating/coupling themselves directly), then the design usually uses the Abstract Factory pattern to create the implementation objects.

/**
 * Create an interface/wrapper class that "has a"
 * implementation object and delegates all requests to it
 */
class Stack {
    protected StackImp imp;
    public Stack(String s) {
        if (s.equals("java")) {
            imp = new StackJava();
        }
        else {
            imp = new StackMine();
        }
    }
    public Stack() {
        this("java");
    }

    public void push(int in) {
        imp.push(in);
    }

    public int pop() {
        return (Integer) imp.pop();
    }

    public boolean isEmpty() {
        return imp.empty();
    }
}

/**
 * Embellish the interface class with derived classes if desired
 */
class StackHanoi extends Stack {
    private int totalRejected = 0;

    public StackHanoi() {
        super("java");
    }

    public StackHanoi(String s) {
        super(s);
    }

    public int reportRejected() {
        return totalRejected;
    }

    public void push(int in) {
        if (!imp.empty() && in > (Integer)imp.peek()) {
            totalRejected++;
        } else {
            imp.push(in);
        }
    }
}

/**
 * Create an implementation/body base class
 */
interface StackImp {
    Object push(Object o);
    Object peek();
    boolean empty();
    Object pop();
}

class StackJava extends java.util.Stack implements StackImp { }

/**
 * Derive the separate implementations from the common abstraction
 */
class StackMine implements StackImp {
    private Object[] items = new Object[20];
    private int total = -1;

    public Object push(Object o) {
        return items[++total] = o;
    }

    public Object peek() {
        return items[total];
    }

    public Object pop() {
        return items[total--];
    }

    public boolean empty() {
        return total == -1;
    }
}

public class BridgeDemo {
    public static void main(String[] args) {
        Stack[] stacks = { new Stack("java"), new Stack("mine"),
                new StackHanoi("java"), new StackHanoi("mine") };
        for (int i=0, num; i < 20; i++) {
            num = (int)(Math.random() * 1000) % 40;
            for (Stack stack : stacks) {
                stack.push(num);
            }
        }
        for (int i=0, num; i < stacks.length; i++) {
            while (!stacks[i].isEmpty()) {
                System.out.print( stacks[i].pop() + "  ");
            }
            System.out.println();
        }
        System.out.println("total rejected is " + ((StackHanoi)stacks[3]).reportRejected());
    }
}
