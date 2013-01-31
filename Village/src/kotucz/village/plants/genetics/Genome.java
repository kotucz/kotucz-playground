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
        
        double tilt = Math.toRadians(parseInstruction(position + 2));
        double panAddition = Math.toRadians(parseInstruction(position + 3));
        double twist = Math.toRadians(parseInstruction(position + 4));
        
        double pan = -panAddition * (numberOfStems + 1.0) / 2.0;
        
        System.out.println("pan: " + pan);
        
        for (int i = 0; i < numberOfStems; i++) {
            pan += panAddition;
            
            plant.grow(stemType, (float)pan, (float)tilt, (float)twist, position + 5);  
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
        genome.genes[1] = 2;
        genome.genes[2] = 1;
        genome.genes[3] = 0;
        genome.genes[4] = 50;
        genome.genes[5] = 50;
        genome.genes[6] = InstructionType.GROW.getNumber();
        genome.genes[7] = 2;
        genome.genes[8] = 1;
        genome.genes[9] = 0;
        genome.genes[10] = 50;
        genome.genes[11] = 50;
        genome.genes[12] = InstructionType.GROW.getNumber();
        genome.genes[13] = 2;
        genome.genes[14] = 1;
        genome.genes[15] = 0;
        genome.genes[16] = 50;
        genome.genes[17] = 50;
        
       // genome.genes[1] = 1;
        
        
        return genome;
    }
    
    
    
}
