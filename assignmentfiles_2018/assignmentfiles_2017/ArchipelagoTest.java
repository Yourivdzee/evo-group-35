import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArchipelagoTest {

    Archipelago archipelago;

    Integer size;

    @Before
    public void init() {
        this.size = 3;
        archipelago = new Archipelago(size,1);
        // Initialize archipelago with islands with 2 individuals each
        for(int i = 0; i < size ; i++){
            Island island = archipelago.islands.get(i);
            island.populate(new Population(2,2,2));
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
    public void testMigrate() {
        Population pop = archipelago.islands.get(0).population;
        pop.sortPopulationByFitness();

        Individual bestIndividualOLd = pop.population.get(pop.population.size() - 1);

        archipelago.migrate("circle");

        Individual bestIndividualNew = pop.population.get(pop.population.size() - 1);

        assertNotEquals(bestIndividualNew,bestIndividualOLd);
    }

    @Test
    public void testIntegrateAllMigrants() {
        archipelago.integrateAllMigrants();
    }
}