package tradeworld;

import java.io.Serializable;

/**
 *
 * @author Kotuc
 */
public class Time implements Serializable {

    private final long millis;

    public Time(long time) {
        this.millis = time;
    }

    public long getMillis() {
        return millis;
    }

    public double getSeconds() {
        return millis / 1000.0;
    }

    public Time sub(Time other) {
        if (other == null) {
            throw new NullPointerException("other");
        }
        return new Time(this.millis - other.millis);
    }

    public Time addSec(double secs) {
        return new Time(this.millis + Math.round(1000*secs));
    }

    public boolean passed(Time limit) {
        if (limit == null) {
            throw new NullPointerException("limit");
        }
        return (this.millis > limit.millis);
    }

    @Override
    public String toString() {
        return millis+"ms";
    }



}
