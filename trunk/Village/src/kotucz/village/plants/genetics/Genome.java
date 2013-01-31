/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kotucz.village.plants.genetics;

import kotucz.village.plants.BeingPlant;
import kotucz.village.plants.BeingStem;
import kotucz.village.plants.Plant;
import kotucz.village.plants.StemType;

/**
 *
 * @author Petr
 */
public class Genome {

    private static final int GENOME_LENGTH = 100;
    private int[] genes = new int[GENOME_LENGTH];
    private static int position = 0;

    public Genome() {
    }

    public Genome(Genome parentalGenome) {
        genes = parentalGenome.genes;
    }

    public int[] getGenome() {
        return genes;
    }

    /**
     * Porforms instruction on the given position, returns the next position
     * after this instruction.
     *
     * @param position position win the genome where the instruction starts
     * @param plant plant on which the instruction is performed
     * @return the next position after instruction
     */
    public void performInstruction(int position, BeingPlant plant, BeingStem stem) {

        System.out.println("Performing instruction " + position + " : " + InstructionType.getInstructionType(parseInstruction(position)));

        switch (InstructionType.getInstructionType(parseInstruction(position))) {
            case NOTHING:
                break;
            case GROW:
                grow(position + 1, plant);
                break;
            case FORK:
                fork(position + 1, plant, stem);
                break;
            case ADD:
                add(position + 1, stem);
                break;
            case SUB:
                sub(position + 1, stem);
                break;
            case JUMP_IF_POSITIVE:
                jump_if_positive(position + 1, plant, stem);
                break;

            default:
                break;

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

        double panAddition = Math.toRadians(parseInstruction(position + 2));
        double tilt = Math.toRadians(parseInstruction(position + 3));
        double twist = Math.toRadians(parseInstruction(position + 4));

        double pan = -panAddition * (numberOfStems + 1.0) / 2.0;

        System.out.println("pan: " + pan);

        for (int i = 0; i < numberOfStems; i++) {
            pan += panAddition;

            plant.grow(stemType, (float) pan, (float) tilt, (float) twist, position + 5);
        }
    }

    private void fork(int position, BeingPlant plant, BeingStem stem) {

        int jump1 = parseInstruction(position);
        int jump2 = parseInstruction(position + 1);

        performInstruction(position + 2 + jump1, plant, stem);
        performInstruction(position + 2 + jump2, plant, stem);
    }

    private void add(int position, BeingStem stem) {

        int registerNumber = parseInstruction(position);
        int value = parseInstruction(position + 1);

        stem.addToRegister(registerNumber, value);
    }

    private void sub(int position, BeingStem stem) {

        int registerNumber = parseInstruction(position);
        int value = parseInstruction(position + 1);

        stem.subFromRegister(registerNumber, value);
    }

    private void jump_if_positive(int position, BeingPlant plant, BeingStem stem) {
        int registerNumber = parseInstruction(position);
        int jumpLength = parseInstruction(position + 1);

        int newPosition = position + 2;

        if (stem.getRegisterValue(registerNumber) > 0) {
            newPosition += jumpLength;
        }

        performInstruction(newPosition, plant, stem);
    }

    public static Genome testGenome1() {
        Genome genome = new Genome();
        position = 0;


        addInstruction(genome, InstructionType.GROW, 2, 1, 50, 0, 50);
        addInstruction(genome, InstructionType.FORK, 0, 3);
        {
            addInstruction(genome, InstructionType.ADD, 1, 3);
            addInstruction(genome, InstructionType.GROW, 2, 1, 50, 0, 50);
            addInstruction(genome, InstructionType.FORK, 0, 3);
            {

                addInstruction(genome, InstructionType.SUB, 1, 1);
                addInstruction(genome, InstructionType.GROW, 2, 1, 50, 0, 50);
                addInstruction(genome, InstructionType.JUMP_IF_POSITIVE, 1, -15);
                addInstruction(genome, InstructionType.GROW, 8, 1, 45, 90, 0);
            }
        }





        // genome.genes[1] = 1;


        return genome;
    }

    private static void addInstruction(Genome genome, InstructionType type, int... values) {
        genome.genes[position] = type.getNumber();

        position += 1;
        for (int i = 0; i < values.length; i++) {
            genome.genes[position + i] = values[i];

        }
        position += values.length;
    }
}
