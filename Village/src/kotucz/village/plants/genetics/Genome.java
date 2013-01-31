/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kotucz.village.plants.genetics;

import kotucz.village.plants.BeingPlant;
import kotucz.village.plants.Plant;
import kotucz.village.plants.StemType;

/**
 *
 * @author Petr
 */
public class Genome {
    
    private static final int GENOME_LENGTH = 100;
    
    private byte[] genes = new byte[GENOME_LENGTH];
    
    public Genome() {
        
    }
    
    public Genome(Genome parentalGenome) {
        genes = parentalGenome.genes; 
    }
    
    public byte[] getGenome() {
        return genes;
    }
    
    /**
     * Porforms instruction on the given position
     * @param position position win the genome where the instruction starts
     * @param plant plant on which the instruction is performed
     */
    public void performInstruction(int position, BeingPlant plant) {
        
        
        
        switch (InstructionType.getInstructionType(getInstruction(position))) {
            case GROW : grow(position + 1, plant);   
                break;
                
            default :
                break;
                
            
            
            
        }
    }
    
    private int getInstruction(int position) {
        if (position < GENOME_LENGTH) {
            return genes[position];
        } else {
            return 0;
        }
    }
    
    private void grow(int position, BeingPlant plant) {
        int numberOfStems = getInstruction(position);
        StemType stemType = StemType.getStemType(getInstruction(position + 1));
        int panAddition = getInstruction(position + 2);
        int tilt = getInstruction(position + 3);
        
        int pan = 0;
        
        for (int i = 0; i < numberOfStems; i++) {
            pan += panAddition;
            
            plant.grow(stemType, pan, tilt);
            
        }
    }
    
}
