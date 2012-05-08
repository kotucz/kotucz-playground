package tradeworld;

import java.io.Serializable;

/**
 *
 * @author Kotuc
 */
public class PlayerId implements Serializable {

    private static final long serialVersionUID = 75823760237632L;

    private final int id;

    public PlayerId(int id) {
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
        final PlayerId other = (PlayerId) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.id;
        return hash;
    }

    

}
