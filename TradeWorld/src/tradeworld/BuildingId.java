package tradeworld;

import java.io.Serializable;

/**
 *
 * @author Kotuc
 */
public class BuildingId implements Serializable {

    private final int id;

    public BuildingId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BuildingId other = (BuildingId) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.id;
        return hash;
    }
}
