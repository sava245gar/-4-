import java.util.concurrent.atomic.AtomicInteger;
public class Main {
    public static void main(String[] args) throws InterruptedException {
        new InterferenceExample().example();
    }
}
class InterferenceExample {
    private static final int HUNDRED_MILLION = 100_000_000;
    private AtomicInteger counter = new AtomicInteger();
    boolean stop(){
        return counter.incrementAndGet() > HUNDRED_MILLION;
    }
    void example() throws InterruptedException {
        InterferenceThread thread1 = new InterferenceThread(this);
        InterferenceThread thread2 = new InterferenceThread(this);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("Expected: " + HUNDRED_MILLION);
        System.out.println("Result: " + thread1.getI());
    }
}
class InterferenceThread extends Thread {
    private final InterferenceExample checker;
    private static AtomicInteger i = new AtomicInteger(0);

    InterferenceThread(InterferenceExample checker) {
        this.checker = checker;
    }

    private void increment() {
        i.incrementAndGet(); //Значение терялось здесь так как i++ было не атомарным и два потока могли взять одновременно взять например значение i=12 и записать i=13 вместо i=14
    }

    int getI() {
        return i.get();
    }

    public void run() {
        while (!checker.stop()) {
            increment();
        }
    }
}