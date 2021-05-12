public class Fork {
    private final int index;
    private boolean available;

    public Fork(int index) {
        this.index = index;
        this.available = true;
    }

    public int getIndex() {
        return index;
    }

    public synchronized void pickUp(int name) throws InterruptedException {
        while (!available) {
            Thread.sleep(100);
        }
        System.out.println("Philosopher " + name + " " + System.nanoTime() + ": Picked up fork " + index);
        available = false;
    }

    public void putDown() {
        available = true;
    }
}
