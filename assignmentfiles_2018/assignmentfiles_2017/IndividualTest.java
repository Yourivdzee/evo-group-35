import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.Arrays;

import static org.junit.Assert.*;

public class IndividualTest {
    Individual individual1;
    Individual individual2;

    @Before
    public void init() {
        individual1 = new Individual();
        individual2 = new Individual();
    }

    @Test
    public void testCreatingRandomIndividual() {
        boolean passed = true;
        Individual individual = new Individual();
        System.out.println("Testing CREATING individual with genotype " + Arrays.toString(individual.genotype));
        for (int i = 0 ; i < individual.genotype.length ; i++){
            if ((individual.genotype[i] < - 5 ) || (individual.genotype[i] > 5))
                passed = false;
        }
        assertTrue(passed);
    }

    @Test
    public void testUniformMutation() {
        Individual individual = new Individual();

        double[] before = individual.genotype;

        System.out.println("Testing MUTATING individual with genotype " + Arrays.toString(individual.genotype));

        individual.uniformMutate(0.2);

        System.out.println("Individual now has genotype " + Arrays.toString(individual.genotype));

        double[] after = individual.genotype;
    }
}
