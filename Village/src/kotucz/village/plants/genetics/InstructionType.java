/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kotucz.village.plants.genetics;

/**
 *
 * @author Petr
 */
public enum InstructionType {
    NOTHING(0), GROW(1);
    
    final int num;
    
    InstructionType(int num) {
        this.num = num;
    }
    
    public static InstructionType getInstructionType(int number) {
        for (InstructionType instructionType : values()) {
            if (instructionType.num == number) {
                return instructionType;
            }
        }
        return InstructionType.NOTHING;
    }
    
    
}
