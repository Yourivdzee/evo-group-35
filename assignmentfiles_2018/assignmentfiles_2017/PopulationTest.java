import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Random;

import static org.junit.Assert.*;

public class PopulationTest {
    Population pop;

    Random rand;

    int popSize = 10;
    int offspringSize = 10;
    int matingPoolSize = 10;

    @Before
    public void init() {
        pop = new Population(popSize, matingPoolSize, offspringSize);
        rand = new Random();
        rand.setSeed(1);
    }

    @Test
    public void testCreateNewPopulation() {
       assertTrue(pop.population.size() == popSize);
    }

    @Test
    public void testSortPopulation() {
        System.out.println(" -- Testing sorting population -- ");

        for(Individual individual: pop.population) {
            individual.fitness = rand.nextDouble();
            System.out.print("Assigning fitness " + Double.toString(individual.fitness) + "\n");
        }


        pop.sortPopulationByFitness();

        System.out.println("First fitness: " + Double.toString(pop.population.get(0).fitness));
        assertTrue(pop.population.get(0).fitness < pop.population.get(1).fitness);
    }

    @Test
    public void testCalculatingLinearReproductionProbability() {
        System.out.println(" -- Testing linear calculation  of reproduction probability -- ");


        for(Individual individual: pop.population) {
            individual.fitness = rand.nextDouble();
        }

        pop.sortPopulationByFitness();

        pop.calculateLinearReproductionProbability(1.5);


        System.out.println("Printing sorted reproduction probabilities: ");

        for(Individual individual: pop.population) {
            System.out.print("For fitness " + Double.toString(individual.fitness) + " reproduction prob: ");
            System.out.println(individual.selection_prob);
        }

        assertTrue(pop.population.get(popSize - 1).selection_prob > pop.population.get(popSize - 2).selection_prob);
    }

    @Test
    public void testCalculatingExponentialReproductionProbabilities() {
        System.out.println(" -- Testing exponential calculation  of reproduction probability -- ");

        for(Individual individual: pop.population) {
            individual.fitness = rand.nextDouble();
        }

        pop.sortPopulationByFitness();

        pop.calculateExponentialReproductionProbability();

        System.out.println("Printing sorted reproduction probabilities: ");

        for(Individual individual: pop.population) {
            System.out.print("For fitness " + Double.toString(individual.fitness) + " reproduction prob: ");
            System.out.println(individual.selection_prob);
        }

        assertTrue(pop.population.get(popSize - 1).selection_prob > pop.population.get(popSize - 2).selection_prob);

    }

    @Test
    public void testCalculateFitnessStatistics() {
        System.out.println(" -- Testing Calculate Fitness Statistics --");

        double sum = 0;
        for (int i = 0; i < pop.populationSize ; i++){
            pop.population.get(i).fitness = i;
            sum = sum + i;
        }

        ArrayList<Double> stats = pop.calculateFitnessStatistics();

        assertTrue(stats.get(0) == sum);
        assertTrue(stats.get(1) == 0);
        assertTrue(stats.get(2) == popSize - 1);
        assertTrue(stats.get(3) == sum/popSize);

    }

    @Test
    public void testCalculateStandardDeviation() {
        System.out.println(" -- Testing Calculate Standard Deviation --");
        double sums = 0;
        for (int i = 0; i < pop.populationSize ; i++){
            pop.population.get(i).fitness = i;
            sums = sums + i;
        }

        double mean = sums/popSize;

        double sum = 0;
        for (int i = 0; i < pop.populationSize ; i++){
            sum = sum + Math.pow(pop.population.get(i).fitness - mean,2);
        }

        double expectedStdDev = Math.sqrt(sum/pop.populationSize);
        System.out.println("Expecting " + Double.toString(expectedStdDev) + " to be equal to " + Double.toString(pop.calculateStandardDeviation()));
        assertTrue(expectedStdDev == pop.calculateStandardDeviation());
    }

    @Test
    public void testCalculateCumulativeReproductionProbability() {
        System.out.println(" -- Testing Calculate Cumulative Reproduction Probability --");
        for (int i = 0; i < pop.populationSize ; i++){
            pop.population.get(i).fitness = i;
        }
        pop.calculateLinearReproductionProbability(1.2);

        ArrayList<Double> cumulativeProbabilities = pop.calculateCumulativeReproductionProbability();

        System.out.println("Cumulative probabilities: " + cumulativeProbabilities.toString());
    }

    @Test
    public void testRouletteWheel() {
        System.out.println(" -- Testing Roulette Wheel --");

        for (int i = 0; i < pop.populationSize ; i++){
            pop.population.get(i).fitness = i;
            System.out.print("Assigning fitness " + Double.toString(pop.population.get(i).fitness) + "\n");
        }

        pop.calculateLinearReproductionProbability(1.2);

        pop.rouletteWheel();

        for(Individual individual: pop.matingPool){
            System.out.println("Individual with fitness " + Double.toString(individual.fitness) + " selected");
        }

    }

    @Test
    public void testStochasticUniversalSampling () {
        System.out.println(" -- Testing Stochastic Universal Sampling --");

        for (int i = 0; i < pop.populationSize ; i++){
            pop.population.get(i).fitness = rand.nextDouble()*pop.populationSize;
            System.out.print("Assigning fitness " + Double.toString(pop.population.get(i).fitness) + "\n");
        }

        pop.calculateLinearReproductionProbability(1.2);

        pop.stochasticUniversalSampling();

        for(Individual individual: pop.matingPool){
            System.out.println("Individual with fitness " + Double.toString(individual.fitness) + "selected");
        }
    }

    @Test
    public void testReplacePopulationByAge() {
        ArrayList<Individual> parents = new ArrayList<>();

        double parentGenotype[] = {
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                1.1
        };

        Individual parent = new Individual(parentGenotype);
        parents.add(parent);

        ArrayList<Individual> offsprings = new ArrayList<>();

        double childGenotype[] = {
                1.1,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0
        };

        Individual offspring = new Individual(childGenotype);
        offsprings.add(offspring);

        pop.offsprings.addAll(offsprings);
        pop.matingPool.addAll(parents);
        pop.population.addAll(parents);

        pop.replacePopulationByAge();
        assertFalse(pop.population.get(0).equals(parent));

    }

    @Test
    public void testReplaceWorst () {
        pop.population.clear();
        int oldSize = pop.populationSize;
        pop.populationSize = 2;
        Individual offspring = new Individual();
        offspring.fitness = 9.0;
        pop.offsprings.add(offspring);

        Individual badParent = new Individual();
        badParent.fitness = 1.0;

        Individual goodParent = new Individual();
        goodParent.fitness = 5.0;

        pop.population.add(badParent);
        pop.population.add(goodParent);

        pop.replaceWorst();

        assertEquals(0, pop.offsprings.size());
        assertEquals(2, pop.population.size());
        assertFalse(pop.population.contains(badParent));

        pop.populationSize = oldSize;
    }

    @Test
    public void tournamentSelection () {
        System.out.print("MISSING TEST");
    }
}
