import org.junit.Before;
import org.junit.Test;
import org.vu.contest.ContestEvaluation;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ArchipelagoTest {

    Archipelago archipelago;

    Integer size;

    @Before
    public void init() {
        ContestEvaluation evaluation_ = null;

        this.size = 3;
        archipelago = new Archipelago(size,1, 4);
        // Initialize archipelago with islands with 2 individuals each
        for(int i = 0; i < size ; i++){
            Island island = archipelago.islands.get(i);
            island.populate(new Population(evaluation_, 2,2,2));
            for(int j = 0; j < island.population.population.size() ; j++){
                island.population.population.get(j).fitness = j + 1;
            }
        }
    }

    @Test
    public void testPopulateIsland() {
        assertNotNull(archipelago.islands.get(0).population);
    }

    @Test
    public void testMigrateCircle() {
        Population pop = archipelago.islands.get(0).population;
        pop.sortPopulationByFitness();

        Individual bestIndividualOLd = pop.population.get(pop.population.size() - 1);

        archipelago.migrate("circle");

        Individual bestIndividualNew = pop.population.get(pop.population.size() - 1);

        assertNotEquals(bestIndividualNew,bestIndividualOLd);
    }

    @Test
    public void testMigrateStar() {
        Population pop = archipelago.islands.get(0).population;
        pop.sortPopulationByFitness();

        Individual bestIndividualOLd = pop.population.get(pop.population.size() - 1);

        archipelago.migrate("star");

        Individual bestIndividualNew = pop.population.get(pop.population.size() - 1);

        assertNotEquals(bestIndividualNew,bestIndividualOLd);
    }

    @Test
    public void testIntegrateAllMigrants() {
        archipelago.integrateAllMigrants();
    }

    @Test
    public void testCheckMigrationStatus() {
        archipelago.islands.get(0).gave = false;
        boolean status = archipelago.checkMigrationStatus();
        assertFalse(status);
    }

    @Test
    public void testCheckConvergence() {
        ArrayList<Double> convergedHistory = new ArrayList<>();
        convergedHistory.add(1.7);
        convergedHistory.add(1.2);
        convergedHistory.add(1.3);
        convergedHistory.add(1.4);
        convergedHistory.add(1.5);
        convergedHistory.add(1.6);
        for(Island island: archipelago.islands){
            island.maxFitnessHistory = convergedHistory;
        }
        assertTrue(archipelago.checkConvergence());
    }
}