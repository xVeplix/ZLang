package ZLang.function.util.Object;

import jdk.internal.HotSpotIntrinsicCandidate;

public class FuncObject {

    @HotSpotIntrinsicCandidate
    public FuncObject() {
    }

    @HotSpotIntrinsicCandidate
    public final native Class<?> getClasses();

    @HotSpotIntrinsicCandidate
    public native int hashCode();

    public boolean equals(Object obj) {
        return (this == obj);
    }

    @HotSpotIntrinsicCandidate
    protected native Object clone() throws CloneNotSupportedException;

    public String toString() {
        return getClasses().getName() + "@" + Integer.toHexString(hashCode());
    }

    @HotSpotIntrinsicCandidate
    public final native void notifys();

    @HotSpotIntrinsicCandidate
    public final native void notifysAll();

    public final void waits() throws InterruptedException {
        waits(0L);
    }

    public final native void waits(long timeoutMillis) throws InterruptedException;

    public final void waits(long timeoutMillis, int nanos) throws InterruptedException {
        if (timeoutMillis < 0) {
            throw new IllegalArgumentException("timeoutMillis value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                    "nanosecond timeout value out of range");
        }

        if (nanos > 0 && timeoutMillis < Long.MAX_VALUE) {
            timeoutMillis++;
        }

        waits(timeoutMillis);
    }

    @Deprecated(since="9")
    protected void finalizes() throws Throwable { }

}
