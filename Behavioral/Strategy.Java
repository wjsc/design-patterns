// Rules of thumb
// Strategy is like Template Method except in its granularity.
// State is like Strategy except in its intent.
// Strategy lets you change the guts of an object. Decorator lets you change the skin.
// State, Strategy, Bridge (and to some degree Adapter) have similar solution structures. They all share elements of the 'handle/body' idiom. They differ in intent - that is, they solve different problems.
// Strategy has 2 different implementations, the first is similar to State. The difference is in binding times (Strategy is a bind-once pattern, whereas State is more dynamic).
// Strategy objects often make good Flyweights.

// 1. Define the interface of the algorithm
interface Strategy {
    void solve();
}

// 2. Bury implementation
@SuppressWarnings("ALL")
abstract class StrategySolution implements Strategy {
    // 3. Template Method
    public void solve() {
        start();
        while (nextTry() && !isSolution()) {}
        stop();
    }

    abstract void start();
    abstract boolean nextTry();
    abstract boolean isSolution();
    abstract void stop();
}

class FOO extends StrategySolution {
    private int state = 1;

    protected void start() {
        System.out.print("Start  ");
    }

    protected void stop() {
        System.out.println("Stop");
    }

    protected boolean nextTry() {
        System.out.print("NextTry-" + state++ + "  ");
        return true;
    }

    protected boolean isSolution() {
        System.out.print("IsSolution-" + (state == 3) + "  ");
        return (state == 3);
    }
}

// 2. Bury implementation
abstract class StrategySearch implements Strategy {
    // 3. Template Method
    public void solve() {
        while (true) {
            preProcess();
            if (search()) {
                break;
            }
            postProcess();
        }
    }

    abstract void preProcess();
    abstract boolean search();
    abstract void postProcess();
}

@SuppressWarnings("ALL")
class BAR extends StrategySearch {
    private int state = 1;

    protected void preProcess()  {
        System.out.print("PreProcess  ");
    }

    protected void postProcess() {
        System.out.print("PostProcess  ");
    }

    protected boolean search() {
        System.out.print("Search-" + state++ + "  ");
        return state == 3 ? true : false;
    }
}

// 4. Clients couple strictly to the interface
public class StrategyDemo {
    // client code here
    private static void execute(Strategy strategy) {
        strategy.solve();
    }

    public static void main( String[] args ) {
        Strategy[] algorithms = {new FOO(), new BAR()};
        for (Strategy algorithm : algorithms) {
            execute(algorithm);
        }
    }
}
