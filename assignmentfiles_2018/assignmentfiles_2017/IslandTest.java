import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {

    Island island1;

    Island island2;

    @Before
    public void init(){
        island1 = new Island(1);
        island2 = new Island(2);
    }

    @Test
    public void populate() {
        Population population = new Population(3,3,3);
        island1.populate(population);
        assertNotNull(island1.population);
    }

    @Test
    public void testGiveBest() {
        island1.give();
    }

    @Test
    public void integrateMigrants() {
    }

    @Test
    public void addMaxFitness() {
    }

    @Test
    public void checkConvergence() {
    }
}