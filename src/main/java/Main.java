import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AbstractFindPathInputReader inputReader;
        System.out.println("Enter\n1 for reading from standard input" +
                " or\n2 for reading from file:");
        String path;
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        if (1 == input) {
            inputReader = new FindPathInputReaderStdIn();
            path = inputReader.readInput();
            System.out.println(path);
            scanner.close();
        } else if (2 == input) {
            System.out.println("2");
            inputReader = new FindPathInputReaderFile();
            path = inputReader.readInput();
            System.out.println(path);
            scanner.close();
        } else {
            scanner.close();
            System.out.println("Invalid input.");
        }
    }

}
