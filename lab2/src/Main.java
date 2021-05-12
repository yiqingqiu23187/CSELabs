import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int p = scanner.nextInt();
        if (n + p == 1) {
            System.out.println(0);
        } else {
            long remain = 1;
            long multiply = 1;
            for (long i = n + p; i >= n + 1; i--) {
                multiply *= i;
                multiply = multiply % n;
                remain += multiply;
            }
            System.out.println(remain % n);
        }
    }
}