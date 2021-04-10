// Rules of thumb
// Adapter makes things work after they're designed; Bridge makes them work before they are.
// Bridge is designed up-front to let the abstraction and the implementation vary independently. Adapter is retrofitted to make unrelated classes work together.
// Adapter provides a different interface to its subject. Proxy provides the same interface. Decorator provides an enhanced interface.
// Adapter is meant to change the interface of an existing object. Decorator enhances another object without changing its interface. Decorator is thus more transparent to the application than an adapter is. As a consequence, Decorator supports recursive composition, which isn't possible with pure Adapters.
// Facade defines a new interface, whereas Adapter reuses an old interface. Remember that Adapter makes two existing interfaces work together as opposed to defining an entirely new one.

/* The OLD */
class SquarePeg {
    private double width;

    public SquarePeg(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}

/* The NEW */
class RoundHole {
    private final int radius;

    public RoundHole(int radius) {
        this.radius = radius;
        System.out.println("RoundHole: max SquarePeg is " + radius * Math.sqrt(2));
    }

    public int getRadius() {
        return radius;
    }
}

// Design a "wrapper" class that can "impedance match" the old to the new
class SquarePegAdapter {
    // The adapter/wrapper class "has a" instance of the legacy class
    private final SquarePeg squarePeg;

    public SquarePegAdapter(double w) {
        squarePeg = new SquarePeg(w);
    }

    // Identify the desired interface
    public void makeFit(RoundHole roundHole) {
        // The adapter/wrapper class delegates to the legacy object
        double amount = squarePeg.getWidth() - roundHole.getRadius() * Math.sqrt(2);
        System.out.println( "reducing SquarePeg " + squarePeg.getWidth() + " by " + ((amount < 0) ? 0 : amount) + " amount");
        if (amount > 0) {
            squarePeg.setWidth(squarePeg.getWidth() - amount);
            System.out.println("   width is now " + squarePeg.getWidth());
        }
    }
}

public class AdapterDemoSquarePeg {
    public static void main( String[] args ) {
        RoundHole roundHole = new RoundHole( 5 );
        SquarePegAdapter squarePegAdapter;
        for (int i = 6; i < 10; i++) {
            squarePegAdapter = new SquarePegAdapter((double)i);
            // The client uses (is coupled to) the new interface
            squarePegAdapter.makeFit(roundHole);
        }
    }
}