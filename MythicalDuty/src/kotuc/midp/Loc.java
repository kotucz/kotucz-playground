package kotuc.midp;

public class Loc {
    
    public int a, b;
    
    /** Creates a new instance of Pos */
    public Loc() {
    }
    
    /** Creates a new instance of Pos */
    public Loc(Loc l) {
        this.a = l.a;
        this.b = l.b;
    }
    
    public Loc(int a, int b) {
        this.a = a;
        this.b = b;
    }
    
    public boolean equals(Loc loc2) {
        return ((this.a==loc2.a)&&(this.b==loc2.b));
    }
    
    public String toString() {
        return "[a:"+a+",b:"+b+"]";
    }
        
}
	