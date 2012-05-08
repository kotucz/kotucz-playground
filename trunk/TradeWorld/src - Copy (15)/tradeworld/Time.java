package tradeworld;

import java.io.Serializable;

/**
 *
 * @author Kotuc
 */
public class Time implements Serializable {

    private final long millis;

    Time(long time) {
        this.millis = time;
    }

    public long getMillis() {
        return millis;
    }

    public double getSeconds() {
        return millis / 1000.0;
    }

    public Time sub(Time other) {
        return new Time(this.millis - other.millis);
    }

    public Time addSec(int secs) {
        return new Time(this.millis + 1000*secs);
    }

    public boolean passed(Time limit) {
        return (this.millis > limit.millis);
    }

    @Override
    public String toString() {
        return millis+"ms";
    }



}
