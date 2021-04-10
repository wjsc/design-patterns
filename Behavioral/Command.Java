// Rules of thumb
// Chain of Responsibility, Command, Mediator, and Observer, address how you can decouple senders and receivers, but with different trade-offs. Command normally specifies a sender-receiver connection with a subclass.
// Chain of Responsibility can use Command to represent requests as objects.
// Command and Memento act as magic tokens to be passed around and invoked at a later time. In Command, the token represents a request; in Memento, it represents the internal state of an object at a particular time. Polymorphism is important to Command, but not to Memento because its interface is so narrow that a memento can only be passed as a value.
// Command can use Memento to maintain the state required for an undo operation.
// MacroCommands can be implemented with Composite.
// A Command that must be copied before being placed on a history list acts as a Prototype.
// Two important aspects of the Command pattern: interface separation (the invoker is isolated from the receiver), time separation (stores a ready-to-go processing request that's to be stated later).

class SimpleCommand {
    private int state;

    public SimpleCommand(int state) {
        this.state = state;
    }

    public void add(Integer value) {
        state += value.intValue();
    }

    public void addTwoOperands(Integer firstValue, Integer secondValue) {
        state = state + firstValue.intValue() + secondValue.intValue();
    }

    public int getState() {
        return state;
    }
}

class ReflectCommand {
    // the "encapsulated" object
    private Object receiver;
    // the "pre-registered" request
    private Method action;
    // the "pre-registered" request
    private Object[] args;

    public ReflectCommand(Object obj, String methodName, Object[] arguments) {
        this.receiver = obj;
        this.args = arguments;
        Class cls = obj.getClass();
        Class[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        try {
            action = cls.getMethod(methodName, argTypes);
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Object execute() {
        try {
            action.invoke(receiver, args);
            return receiver.getClass().getMethod("getState").invoke(receiver);
        }
        catch(IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}

public class CommandDemo {
    public static void main(String[] args) {
        SimpleCommand[] simpleCommands = {new SimpleCommand(1), new SimpleCommand(2)};
        System.out.print("Normal call results: ");
        simpleCommands[0].add(3);
        System.out.print(simpleCommands[0].getState() + " ");
        simpleCommands[1].addTwoOperands(4, 5);
        System.out.print(simpleCommands[1].getState());

        ReflectCommand[] reflectCommands = {
                new ReflectCommand(simpleCommands[0], "add", new Integer[] {3}),
                new ReflectCommand(simpleCommands[1], "addTwoOperands", new Integer[] {4, 5})
        };
        System.out.print("\nReflection results:  ");
        for (ReflectCommand command : reflectCommands) {
            System.out.print(command.execute() + " ");
        }
        System.out.println();
    }
}