// Rules of thumb
// Composite and Decorator have similar structure diagrams, reflecting the fact that both rely on recursive composition to organize an open-ended number of objects.
// Composite can be traversed with Iterator. Visitor can apply an operation over a Composite. Composite could use Chain of Responsibility to let components access global properties through their parent. It could also use Decorator to override these properties on parts of the composition. It could use Observer to tie one object structure to another and State to let a component change its behavior as its state changes.
// Composite can let you compose a Mediator out of smaller pieces through recursive composition.
// Decorator is designed to let you add responsibilities to objects without subclassing. Composite's focus is not on embellishment but on representation. These intents are distinct but complementary. Consequently, Composite and Decorator are often used in concert.
// Flyweight is often combined with Composite to implement shared leaf nodes.

// 1. "lowest common denominator"
interface Component {
    void traverse();
}

// 2. "Isa" relationship
class Primitive implements Component {
    private int value;

    public Primitive(int val) {
        value = val;
    }

    public void traverse() {
        System.out.print( value + "  " );
    }
}

// 2. "Isa" relationship
abstract class Composite implements Component {
    // 3. Couple to interface
    private Component[] children = new Component[9];
    private int total = 0;
    private int value;
    public Composite(int val) {
        value = val;
    }

    // 3. Couple to interface
    public void add(Component c) {
        children[total++] = c;
    }

    public void traverse() {
        System.out.print(value + "  ");
        for (int i=0; i < total; i++) {
            // 4. Delegation and polymorphism
            children[i].traverse();
        }
    }
}

// Two different kinds of "container" classes.  Most of the
// "meat" is in the Composite base class.
class Row extends Composite {
    public Row(int val) {
        super(val);
    }

    public void traverse() {
        System.out.print("Row");
        super.traverse();
    }
}

class Column extends Composite {
    public Column(int val) {
        super(val);
    }

    public void traverse() {
        System.out.print("Col");
        super.traverse();
    }
}

public class CompositeDemo {
    public static void main( String[] args ) {
        Composite first  = new Row( 1 );
        Composite second = new Column( 2 );
        Composite third  = new Column( 3 );
        Composite fourth = new Row( 4 );
        Composite fifth  = new Row( 5 );
        first.add(second);
        first.add(third);
        third.add(fourth);
        third.add(fifth);
        first.add(new Primitive(6));
        second.add(new Primitive(7));
        third.add(new Primitive(8));
        fourth.add(new Primitive(9));
        fifth.add(new Primitive(10));
        first.traverse();
    }
}
