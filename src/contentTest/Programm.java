package contentTest;

public class Programm {

    // Dont let initialize the Class
    private Programm() {
    }

    // Causes the currently executing thread to sleep (temporarily cease execution) for the specified number of milliseconds, subject to the precision and accuracy of system timers and schedulers. The thread does not lose ownership of any monitors.
    public static void sleep(final int millis) {
        try { Thread.sleep(millis); } catch (InterruptedException e) { Console.print("[Programm: sleep] > Something went wrong:"); }
    }

}
