public class ImpossiblePathException extends Exception {
    public ImpossiblePathException() {
        super();
        System.out.println("Error: It is impossible to reach the end from the start.");
        System.exit(1);
    }
}
