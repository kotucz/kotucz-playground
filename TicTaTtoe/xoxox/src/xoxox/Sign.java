package xoxox;

/**
 *
 * @author xkotula
 */
public enum Sign {

    X, O, FREE {
        
        @Override
        public String toString() {
            return ".";
        }
    };

    public static Sign parse(char c) {
        switch (c) {
            case 'X':
                return X;
            case 'O':
                return O;
            default:
                return FREE;

        }
    }

    public Sign opp() {
        switch (this) {
            case X:
                return O;
            case O:
                return X;
            default:
                return FREE;

        }
    }

}
