import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int p = scanner.nextInt();
        int[] temp = new int[n + p];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = (i + 1) % n;
        }

        double result;
        double a = 1;
        for (int i = 0; i < temp.length; i++) {
            a *= temp[i];
        }

        result = (2 * a + ((Math.exp(1) - 2) * a) % n)%n;

        System.out.println((int) result);
    }
}
