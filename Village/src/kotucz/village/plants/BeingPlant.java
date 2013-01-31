/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kotucz.village.plants;

/**
 *
 * @author Petr
 */
public interface BeingPlant {
    
    /**
     * Grows new part of the plant.
     * @param stemType type of the new grown stem.
     * @param pan panoramatic angle of the new stem. It is the deviation form the panoramatic angle of the parent stem.
     * @param tilt vertical angle of the new stem. Zero means the same direction as the parent stem.
     * @param positionOfInstruction positionnof the instruction for the new grown part.
     */
    public void grow(StemType stemType, float pan, float tilt, int positionOfInstruction);
    
}
