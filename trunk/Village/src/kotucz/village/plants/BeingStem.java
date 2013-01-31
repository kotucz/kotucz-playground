/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kotucz.village.plants;

/**
 *
 * @author Petr
 */
public interface BeingStem {
    
    /**
     * Sets the position of the instruction for this stem.
     * @param position position of the instruction in the plant genome.
     */
    public void setGenomePosition(int position);
    
    /**
     * Gets the position of the instructionn for this stem.
     * @return position of the instructionn for this stem in the plant genome.
     */
    public int getGenomePosition();
    
    /**
     * Adds to the local register.
     * @param registerNumber
     * @param value 
     */
    public void addToRegister(int registerNumber, int value); 
    
    /**
     * Subtracts from the local register.
     * @param registerNumber
     * @param value 
     */
    public void subFromRegister(int registerNumber, int value);
    
    /**
     * Gets the value form local register.
     * @param registerNumber
     * @return 
     */
    public int getRegisterValue(int registerNumber);
    
}
