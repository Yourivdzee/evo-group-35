import org.junit.Before;
import org.junit.Test;
import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {

    Island island1;

    Island island2;

    @Before
    public void init(){
        ContestEvaluation evaluation_ = null;

        island1 = new Island(1);
        island2 = new Island(2);
        Population population1 = new Population(evaluation_, 3,3,3);
        island1.populate(population1);
        Population population2 = new Population(evaluation_, 3,3,3);
        island2.populate(population2);
    }

    @Test
    public void populate() {
        assertNotNull(island1.population);
    }

    @Test
    public void testGiveBest() {
        for(int i = 0; i < 3; i++){
            island1.population.population.get(i).fitness = i;
            island2.population.population.get(i).fitness = i + 1;
        }
        island1.population.sortPopulationByFitness();
        Individual bestPop1Individual = island1.population.population.get(island1.population.population.size()-1);
        island1.give(island2,1,"best");
        assertFalse(island2.newIndividuals.size() == 0);
        assertTrue(island2.newIndividuals.contains(bestPop1Individual));
    }

    @Test
    public void testGiveSecondBest() {
        for(int i = 0; i < 3; i++){
            island1.population.population.get(i).fitness = i;
            island2.population.population.get(i).fitness = i + 1;
        }
        island1.population.sortPopulationByFitness();
        Individual bestPop1Individual = island1.population.population.get(island1.population.population.size()-2);
        island1.give(island2,1,"second_best");
        assertFalse(island2.newIndividuals.size() == 0);
        assertTrue(island2.newIndividuals.contains(bestPop1Individual));
    }
    @Test
    public void integrateMigrants() {
        for(int i = 0; i < 3; i++){
            island1.population.population.get(i).fitness = i;
            island2.population.population.get(i).fitness = i + 1;
        }

        island1.give(island2,1,"best");
        island2.give(island1,1,"best");

        assertFalse(island2.newIndividuals.size() == 0);

        island1.integrateMigrants();
        island2.integrateMigrants();


        assertEquals(0, island2.newIndividuals.size());
    }

    @Test
    public void addMaxFitness() {
        assertEquals(0, island1.maxFitnessHistory.size());
        for(int i = 0; i < 3; i++){
            island1.population.population.get(i).fitness = i;
            island2.population.population.get(i).fitness = i + 1;
        }
        island1.addMaxFitness();
        assertEquals(1, island1.maxFitnessHistory.size());
    }

    @Test
    public void checkConvergence() {
        ArrayList<Double> convergedHistory = new ArrayList<>();
        convergedHistory.add(1.7);
        convergedHistory.add(1.2);
        convergedHistory.add(1.3);
        convergedHistory.add(1.4);
        convergedHistory.add(1.5);
        convergedHistory.add(1.6);
        island1.maxFitnessHistory = convergedHistory;
        assertTrue(island1.checkConvergence(5));
        assertFalse(island2.checkConvergence(5));
    }
}