package tradeworld.multi;

import java.io.Serializable;

/**
 *
 * @author Kotuc
 */
public class ClientId implements Serializable {

    final int id;
    final String name;

    public ClientId(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientId other = (ClientId) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.id;
        return hash;
    }

    

}
