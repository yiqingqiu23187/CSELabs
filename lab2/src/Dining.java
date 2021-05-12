public class Dining {
    public static void main(String[] args) throws Exception {
        Philosopher[] philosophers = new Philosopher[5];
        Fork[] forks = new Fork[philosophers.length];
        for (int i = 0; i < forks.length; i++) {
            // initialize fork object
            forks[i] = new Fork(i);
        }
        for (int i = 0; i < philosophers.length; i++) {
            // initialize Philosopher object
            philosophers[i] = new Philosopher(i, forks[i], forks[(i + 1) % philosophers.length]);
        }


        Thread[] threads = new Thread[philosophers.length];
        for (int i = 0; i < philosophers.length; i++) {
            threads[i] = new Thread(philosophers[i]);
        }
        for (int i = 0; i < philosophers.length; i++) {
            threads[i].start();
        }
    }
}