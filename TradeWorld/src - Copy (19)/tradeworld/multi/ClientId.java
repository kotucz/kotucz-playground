package tradeworld.multi;

import java.io.Serializable;

/**
 *
 * @author Kotuc
 */
public class ClientId implements Serializable {

    final int id;
    
    public ClientId(int id) {
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

    @Override
    public String toString() {
        return ""+id;
    }

    

}
