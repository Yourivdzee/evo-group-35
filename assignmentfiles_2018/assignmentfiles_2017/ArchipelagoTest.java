import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArchipelagoTest {

    Archipelago archipelago;

    Integer size;

    @Before
    public void init() {
        this.size = 3;
        archipelago = new Archipelago(size);
    }

    @Test
    public void testPopulateIsland() {
        for(int i = 0; i < size ; i++){
            this.archipelago.islands.get(i).populate(new Population(5,5,5));
            assertNotNull(archipelago.islands.get(i).population);
        }
    }

    @Test
    public void testMigrate() {
        for(int i = 0; i < size ; i++){
            Island island = archipelago.islands.get(i);
            island.populate(new Population(5,5,5));
            for(int j = 0; j < island.population.population.size() ; j++){
                Individual individual = island.population.population.get(i);
                individual.fitness = j;
            }
        }
        Population pop = archipelago.islands.get(0).population;
        pop.sortPopulationByFitness();
        Individual bestIndividualOLd = pop.population.get(pop.population.size() - 1);
        archipelago.migrate("circle");
        Individual bestIndividualNew = pop.population.get(pop.population.size() - 1);
        assertNotEquals(bestIndividualNew,bestIndividualOLd);
    }

    @Test
    public void testIntegrateAllMigrants() {
    }
}