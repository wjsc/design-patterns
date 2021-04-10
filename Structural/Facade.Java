// Rules of thumb
// Facade defines a new interface, whereas Adapter uses an old interface. Remember that Adapter makes two existing interfaces work together as opposed to defining an entirely new one.
// Whereas Flyweight shows how to make lots of little objects, Facade shows how to make a single object represent an entire subsystem.
// Mediator is similar to Facade in that it abstracts functionality of existing classes. Mediator abstracts/centralizes arbitrary communications between colleague objects. It routinely "adds value", and it is known/referenced by the colleague objects. In contrast, Facade defines a simpler interface to a subsystem, it doesn't add new functionality, and it is not known by the subsystem classes.
// Abstract Factory can be used as an alternative to Facade to hide platform-specific classes.
// Facade objects are often Singletons because only one Facade object is required.
// Adapter and Facade are both wrappers; but they are different kinds of wrappers. The intent of Facade is to produce a simpler interface, and the intent of Adapter is to design to an existing interface. While Facade routinely wraps multiple objects and Adapter wraps a single object; Facade could front-end a single complex object and Adapter could wrap several legacy objects.


// 1. Subsystem
class PointCartesian {
    private double x, y;
    public PointCartesian(double x, double y ) {
        this.x = x;
        this.y = y;
    }

    public void  move( int x, int y ) {
        this.x += x;
        this.y += y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

// 1. Subsystem
class PointPolar {
    private double radius, angle;

    public PointPolar(double radius, double angle) {
        this.radius = radius;
        this.angle = angle;
    }

    public void  rotate(int angle) {
        this.angle += angle % 360;
    }

    public String toString() {
        return "[" + radius + "@" + angle + "]";
    }
}

// 1. Desired interface: move(), rotate()
class Point {
    // 2. Design a "wrapper" class
    private PointCartesian pointCartesian;

    public Point(double x, double y) {
        pointCartesian = new PointCartesian(x, y);
    }

    public String toString() {
        return pointCartesian.toString();
    }

    // 4. Wrapper maps
    public void move(int x, int y) {
        pointCartesian.move(x, y);
    }

    public void rotate(int angle, Point o) {
        double x = pointCartesian.getX() - o.pointCartesian.getX();
        double y = pointCartesian.getY() - o.pointCartesian.getY();
        PointPolar pointPolar = new PointPolar(Math.sqrt(x * x + y * y),Math.atan2(y, x) * 180 / Math.PI);
        // 4. Wrapper maps
        pointPolar.rotate(angle);
        System.out.println("  PointPolar is " + pointPolar);
        String str = pointPolar.toString();
        int i = str.indexOf('@');
        double r = Double.parseDouble(str.substring(1, i));
        double a = Double.parseDouble(str.substring(i + 1, str.length() - 1));
        pointCartesian = new PointCartesian(r*Math.cos(a*Math.PI/180) + o.pointCartesian.getX(),
                r*Math.sin(a * Math.PI / 180) + o.pointCartesian.getY());
    }
}

class Line {
    private Point o, e;
    public Line(Point ori, Point end) {
        o = ori;
        e = end;
    }

    public void  move(int x, int y) {
        o.move(x, y);
        e.move(x, y);
    }

    public void  rotate(int angle) {
        e.rotate(angle, o);
    }

    public String toString() {
        return "origin is " + o + ", end is " + e;
    }
}

public class FacadeDemo {
    public static void main(String[] args) {
        // 3. Client uses the Facade
        Line lineA = new Line(new Point(2, 4), new Point(5, 7));
        lineA.move(-2, -4);
        System.out.println( "after move:  " + lineA );
        lineA.rotate(45);
        System.out.println( "after rotate: " + lineA );
        Line lineB = new Line( new Point(2, 1), new Point(2.866, 1.5));
        lineB.rotate(30);
        System.out.println("30 degrees to 60 degrees: " + lineB);
    }
}