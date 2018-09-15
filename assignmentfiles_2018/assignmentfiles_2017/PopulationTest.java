import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.Arrays;

import java.util.Random;

import static org.junit.Assert.*;

public class PopulationTest {
    Population pop;

    Random rand;

    @Before
    public void init() {
        pop = new Population(5);
        rand = new Random();
        rand.setSeed(1);
    }

    @Test
    public void testCreateNewPopulation() {
       assertTrue(pop.population.size() == 5);
    }

    @Test
    public void testSortPopulation() {
        System.out.println("Population before sorting");

        for(Individual individual: pop.population) {
            individual.fitness = rand.nextDouble();
        }

        pop.sortPopulationByFitness();
    }
}
