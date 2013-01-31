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
    
}
