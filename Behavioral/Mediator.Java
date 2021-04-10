// Rules of thumb
// Chain of Responsibility, Command, Mediator, and Observer, address how you can decouple senders and receivers, but with different trade-offs. Chain of Responsibility passes a sender request along a chain of potential receivers. Command normally specifies a sender-receiver connection with a subclass. Mediator has senders and receivers reference each other indirectly. Observer defines a very decoupled interface that allows for multiple receivers to be configured at run-time.
// Mediator and Observer are competing patterns. The difference between them is that Observer distributes communication by introducing "observer" and "subject" objects, whereas a Mediator object encapsulates the communication between other objects. We've found it easier to make reusable Observers and Subjects than to make reusable Mediators.
// On the other hand, Mediator can leverage Observer for dynamically registering colleagues and communicating with them.
// Mediator is similar to Facade in that it abstracts functionality of existing classes. Mediator abstracts/centralizes arbitrary communication between colleague objects, it routinely "adds value", and it is known/referenced by the colleague objects (i.e. it defines a multidirectional protocol). In contrast, Facade defines a simpler interface to a subsystem, it doesn't add new functionality, and it is not known by the subsystem classes (i.e. it defines a unidirectional protocol where it makes requests of the subsystem classes but not vice versa).

/ 1. The "intermediary"
class Mediator {
    // 4. The Mediator arbitrates
    private boolean slotFull = false;
    private int number;

    public synchronized void storeMessage(int num) {
        // no room for another message
        while (slotFull == true) {
            try {
                wait();
            }
            catch (InterruptedException e ) {
                Thread.currentThread().interrupt();
            }
        }
        slotFull = true;
        number = num;
        notifyAll();
    }

    public synchronized int retrieveMessage() {
        // no message to retrieve
        while (slotFull == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        slotFull = false;
        notifyAll();
        return number;
    }
}

class Producer implements Runnable {
    // 2. Producers are coupled only to the Mediator
    private Mediator med;
    private int id;
    private static int num = 1;

    public Producer(Mediator m) {
        med = m;
        id = num++;
    }

    @Override
    public void run() {
        int num;
        while (true) {
            med.storeMessage(num = (int)(Math.random()*100));
            System.out.print( "p" + id + "-" + num + "  " );
        }
    }
}

class Consumer implements Runnable {
    // 3. Consumers are coupled only to the Mediator
    private Mediator med;
    private int    id;
    private static int num = 1;

    public Consumer(Mediator m) {
        med = m;
        id = num++;
    }

    @Override
    public void run() {
        while (true) {
            System.out.print("c" + id + "-" + med.retrieveMessage() + "  ");
        }
    }
}

public class MediatorDemo {
    public static void main( String[] args ) {
        List<Thread> producerList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press ENTER for exit");
        Mediator mb = new Mediator();
        producerList.add(new Thread(new Producer(mb)));
        producerList.add(new Thread(new Producer(mb)));
        producerList.add(new Thread(new Consumer(mb)));
        producerList.add(new Thread(new Consumer(mb)));
        producerList.add(new Thread(new Consumer(mb)));
        producerList.add(new Thread(new Consumer(mb)));
        for (Thread p : producerList) {
            p.start();
        }
        boolean stop = false;
        String exit = scanner.nextLine();
        while (!stop) {
            if (exit.equals("")) {
                stop = true;
                for (Thread p : producerList) {
                    //noinspection deprecation
                    p.stop();
                }
            }
        }
    }
}
