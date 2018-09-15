import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class IndividualTest {
    Individual individual1;
    Individual individual2;

    @Before
    public void init() {
        individual1 = new Individual(1);
        individual2 = new Individual(2);
    }

    @Test
    public void testCreatingRandomIndividual() {
        System.out.println(" -- Testing creating random individual --");
        boolean passed = true;
        Individual individual = new Individual(1);
        System.out.println("Created individual: " + Arrays.toString(individual.genotype));
        for (int i = 0 ; i < individual.genotype.length ; i++){
            if ((individual.genotype[i] < - 5 ) || (individual.genotype[i] > 5))
                passed = false;
        }
        assertTrue(passed);
    }

    @Test
    public void testUniformMutation() {
        System.out.println(" -- Testing uniform mutation --");

        double[] before = individual1.genotype.clone();

        System.out.println("Before: " + Arrays.toString(before));

        individual1.uniformMutate(0.2);


        double[] after = individual1.genotype.clone();

        System.out.println("After: " + Arrays.toString(after));

        assertFalse(Arrays.equals(before, after));
    }

    @Test
    public void testNonUniformMutation() {
        System.out.println(" -- Testing non uniform mutation --");

        double[] before = individual1.genotype.clone();

        System.out.println("Before: " + Arrays.toString(before));

        individual1.nonUniformMutate(0.5,0);


        double[] after = individual1.genotype.clone();

        System.out.println("After: " + Arrays.toString(after));

        assertFalse(Arrays.equals(before, after));

    }

    @Test
    public void simpleArithmeticRecombination () {
        System.out.println(" -- Testing simple arithmetic recombination --");
        ArrayList<Individual> offsprings = individual1.mate(individual2, "simple-arith");
        System.out.println("Parent 1: " + Arrays.toString(individual1.genotype));
        System.out.println("Parent 2: " + Arrays.toString(individual2.genotype));
        System.out.println("Offspring 1: " + Arrays.toString(offsprings.get(0).genotype));
        System.out.println("Offspring 2: "+ Arrays.toString(offsprings.get(1).genotype));

        assertTrue(offsprings.get(0).genotype[0] == individual1.genotype[0]);
        assertTrue(offsprings.get(1).genotype[0] == individual2.genotype[0]);
        assertTrue(offsprings.get(0).genotype[9] == (individual1.genotype[9] + individual2.genotype[9])/2);

    }

    @Test
    public void singleArithmeticRecombination () {
        System.out.println(" -- Testing simple arithmetic recombination --");
        ArrayList<Individual> offsprings = individual1.mate(individual2, "single-arith");

        System.out.println("Parent 1: " + Arrays.toString(individual1.genotype));
        System.out.println("Parent 2: " + Arrays.toString(individual2.genotype));
        System.out.println("Offspring 1: " + Arrays.toString(offsprings.get(0).genotype));
        System.out.println("Offspring 1: "+ Arrays.toString(offsprings.get(1).genotype));

        boolean passed = false;
        for (int i = 0; i < offsprings.get(0).genotype.length ; i++){
            if( offsprings.get(0).genotype[i] == offsprings.get(1).genotype[i])
                passed = true;
        }
        assertTrue(passed);

    }

    @Test
    public void wholeArithmeticRecombination() {
        System.out.println(" -- Testing whole Arithmetic Recombination --");
        ArrayList<Individual> offsprings = individual1.mate(individual2, "whole-arith");

        System.out.println("Parent 1: " + Arrays.toString(individual1.genotype));
        System.out.println("Parent 2: " + Arrays.toString(individual2.genotype));
        System.out.println("Offspring 1: " + Arrays.toString(offsprings.get(0).genotype));
        System.out.println("Offspring 1: "+ Arrays.toString(offsprings.get(1).genotype));

        assertTrue(offsprings.get(0).genotype[0] == (individual1.genotype[0] + individual2.genotype[0]) / 2);
    }

    @Test
    public void blendCrossover() {
        System.out.println(" -- Testing blend crossover  --");
        ArrayList<Individual> offsprings = individual1.mate(individual2, "BLX");

        System.out.println("Parent 1: " + Arrays.toString(individual1.genotype));
        System.out.println("Parent 2: " + Arrays.toString(individual2.genotype));
        System.out.println("Offspring 1: " + Arrays.toString(offsprings.get(0).genotype));
        System.out.println("Offspring 1: "+ Arrays.toString(offsprings.get(1).genotype));


    }
}
