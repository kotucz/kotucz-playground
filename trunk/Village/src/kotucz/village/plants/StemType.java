/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kotucz.village.plants;


/**
 *
 * @author Petr
 */
public enum StemType {
    STEM(0), LEAF(1);
    
    final int num;
    
    StemType(int num) {
        this.num = num;
    }
    
    public static StemType getStemType(int number) {
        for (StemType stemType : values()) {
            if (stemType.num == number) {
                return stemType;
            }
        }
        return StemType.STEM;
    }
}
