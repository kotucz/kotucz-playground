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
    NOTHING(0), GROW(1),
    
    FORK(11), //forks program, 
    ADD(12), //adds value to the register
    SUB(13), //subtracts value from the register 
    JUMP_IF_POSITIVE(14); //reads given register and jumps by x if its value is positive, continues else.
    
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
    
    public int getNumber() {
        return num;
    }
    
}
