public class Philosopher implements Runnable {
    private final Fork evenFork;
    private final Fork oddFork;
    private final int name;

    Philosopher(int name, Fork left, Fork right) {
        this.name = name;
        if (left.getIndex() % 2 == 0) {
            evenFork = left;
            oddFork = right;
        } else {
            evenFork = right;
            oddFork = left;
        }
    }

    private void doAction(String action) throws InterruptedException {
//        System.out.println(Thread.currentThread().getName() + " " +
//                action);
        System.out.println("Philosopher " + this.name + " " + action);
        Thread.sleep(((int) (Math.random() * 100)));
    }

    @Override
    public void run() {
        try {
            while (true) {
                doAction(System.nanoTime() + ": Thinking"); // thinking
                // your code
                pickupevenFork();
                pickupoddFork();
                doAction(System.nanoTime() + ": Eating");
                putdownevenfork();//保持拿叉子时一样的顺序
                putdownoddfork();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void pickupevenFork() throws InterruptedException {
        evenFork.pickUp(name);
    }

    private void pickupoddFork() throws InterruptedException {
        oddFork.pickUp(name);
    }

    private void putdownevenfork() {
        evenFork.putDown();
    }

    private void putdownoddfork() {
        oddFork.putDown();
    }
}