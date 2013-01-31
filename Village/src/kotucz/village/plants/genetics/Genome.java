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
    
    private int[] genes = new int[GENOME_LENGTH];
    
    public Genome() {
        
    }
    
    public Genome(Genome parentalGenome) {
        genes = parentalGenome.genes; 
    }
    
    public int[] getGenome() {
        return genes;
    }
    
    /**
     * Porforms instruction on the given position, returns the next position after this instruction.
     * @param position position win the genome where the instruction starts
     * @param plant plant on which the instruction is performed
     * @return the next position after instruction
     */
    public void performInstruction(int position, BeingPlant plant) {
             
        System.out.println("Performing instruction " + position + " : " + parseInstruction(position));
        
        switch (InstructionType.getInstructionType(parseInstruction(position))) {
            case NOTHING : break;
            case GROW : grow(position + 1, plant); 
                break;
            case FORK : fork(position + 1, plant); 
                break;
               
            default : break;
                
        }
    }
    
    private int parseInstruction(int position) {
        if (position < GENOME_LENGTH) {
            return genes[position];
        } else {
            return 0;
        }
    }
    
    private void grow(int position, BeingPlant plant) {
        int numberOfStems = parseInstruction(position);
        StemType stemType = StemType.getStemType(parseInstruction(position + 1));
        int panAddition = parseInstruction(position + 2);
        int tilt = parseInstruction(position + 3);
        
        int pan = 0;
        
        for (int i = 0; i < numberOfStems; i++) {
            pan += panAddition;
            
            plant.grow(stemType, pan, tilt, position + 4);  
        }
    }
    
    private void fork(int position, BeingPlant plant) {
        
        int jump1 = parseInstruction(position);
        int jump2 = parseInstruction(position + 1);
        
        performInstruction(position + 2 + jump1, plant);
        performInstruction(position + 2 + jump2, plant);
    }
    
    
    public static Genome testGenome1() {
        Genome genome = new Genome();
        
        genome.genes[0] = InstructionType.GROW.getNumber();
        genome.genes[1] = 1;
        genome.genes[2] = 1;
        genome.genes[3] = 1;
        genome.genes[4] = 1;
        genome.genes[5] = InstructionType.FORK.getNumber();
        genome.genes[6] = -7;
        genome.genes[7] = 0;
        
       // genome.genes[1] = 1;
        
        
        return genome;
    }
    
    
    
}
