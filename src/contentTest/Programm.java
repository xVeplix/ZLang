package contentTest;

public class Programm {

    public static void sleep(final int millis) {
        try { Thread.sleep(millis); } catch (InterruptedException e) { Console.print("[Programm: sleep] > Something went wrong:"); }
    }

}
