// Rules of thumb
// State objects are often Singletons.
// Flyweight explains when and how State objects can be shared.
// Interpreter can use State to define parsing contexts.
// Strategy has 2 different implementations, the first is similar to State. The difference is in binding times (Strategy is a bind-once pattern, whereas State is more dynamic).
// The structure of State and Bridge are identical (except that Bridge admits hierarchies of envelope classes, whereas State allows only one). The two patterns use the same structure to solve different problems: State allows an object's behavior to change along with its state, while Bridge's intent is to decouple an abstraction from its implementation so that the two can vary independently.
// The implementation of the State pattern builds on the Strategy pattern. The difference between State and Strategy is in the intent. With Strategy, the choice of algorithm is fairly stable. With State, a change in the state of the "context" object causes it to select from its "palette" of Strategy objects.


abstract class State {
    public void pull(Chain wrapper) {
        wrapper.setState(new Off());
        System.out.println("   turning off");
    }
}

class Chain {
    private State current;

    public Chain() {
        current = new Off();
    }

    public void setState(State state) {
        current = state;
    }

    public void pull() {
        current.pull(this);
    }
}

class Off extends State {
    public void pull(Chain wrapper) {
        wrapper.setState(new Low());
        System.out.println( "   low speed" );
    }
}

class Low extends State {
    public void pull(Chain wrapper) {
        wrapper.setState(new Medium());
        System.out.println("   medium speed");
    }
}

class Medium extends State {
    public void pull(Chain wrapper) {
        wrapper.setState(new High());
        System.out.println("   high speed");
    }
}

class High extends State { }

public class StateDemo {
    public static void main( String[] args ) throws IOException {
        InputStreamReader is = new InputStreamReader( System.in );
        Chain chain = new Chain();
        while (true) {
            System.out.print( "Press 'Enter'" );
            is.read();
            chain.pull();
        }
    }
}