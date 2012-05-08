package tradeworld;

import java.io.Serializable;
import tradeworld.multi.ClientId;

/**
 *
 * @param <T>
 * @author Kotuc
 */
public class ObjectId<T> implements Serializable {

    private static final long serialVersionUID = 9752318957289175L;
    private final Class<?> clas;
    private final int uid;
    private final ClientId clientid;

    public ObjectId(T obj, int uid, ClientId client) {
        this.clas = obj.getClass();
        this.uid = uid;
        this.clientid = client;
    }

    public ObjectId(T obj, int uid) {
        this(obj, uid, null);
    }

    /**
     *
     * @param <T>
     * @param obj
     * @return
     */
    public static <T> ObjectId<T> createUnique(T obj) {
        return new ObjectId<T>(obj, 3);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ObjectId<T> other = (ObjectId<T>) obj;
        if (this.clas != other.clas && (this.clas == null || !this.clas.equals(other.clas))) {
            return false;
        }
        if (this.uid != other.uid) {
            return false;
        }
        if (this.clientid != other.clientid && (this.clientid == null || !this.clientid.equals(other.clientid))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.clas != null ? this.clas.hashCode() : 0);
        hash = 79 * hash + this.uid;
        hash = 79 * hash + (this.clientid != null ? this.clientid.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return clas.getSimpleName() + " " + uid + "-" + clientid;
    }
}
