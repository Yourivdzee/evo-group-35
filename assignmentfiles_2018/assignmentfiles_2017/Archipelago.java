import java.util.ArrayList;
import java.util.Random;

public class Archipelago {
    int size;

    int epoch; // 25-150 recommended

    int numExchangeIndividuals; // 2-5 recommended

    int age;

    Random rand;

    ArrayList<Island> islands;

    ArrayList<Island> migrationPool;

    public Archipelago(Integer size, Integer numExchangeIndividuals, Integer epoch) {
        this.size = size;
        this.epoch = epoch;
        this.numExchangeIndividuals = numExchangeIndividuals;
        this.islands = new ArrayList<>();
        this.rand = new Random();
        this.migrationPool = new ArrayList<>();

        for(int i = 0; i < size ; i++)
            islands.add(new Island(i));
    }

    public void populateIsland(int i, Population population){
        islands.get(i).populate(population);
    }

    /**
     * Migrates the populations assuming a strategy.
     * Possible strategies are:
     *  - Star
     *  - Circle
     *  - Closest
     */
    public void migrate(String strat){
        switch (strat){
            case "circle":
                for(int i = 0; i < islands.size(); i++){
                    int current = i%(islands.size());
                    int next = (i+1)%(islands.size());
                    islands.get(current).give(islands.get(next), numExchangeIndividuals, "best");
                }
                break;
            case "star":
                for (Island island: islands ) {
                    Island randomIsland = islands.get(rand.nextInt(islands.size()));
                    while (randomIsland.recieved) {
                        randomIsland = islands.get(rand.nextInt(islands.size()));
                    }
                    island.give(randomIsland, numExchangeIndividuals, "best");
                }
                break;
        }

        assert checkMigrationStatus();


    }

    /**
     * Checks that all islands have given and recieved individuals
     * Used as an assertion.
     * @return: true if all islands have given/recieved individuals.
     */
    public boolean checkMigrationStatus() {
        boolean checked = true;
        for (Island island: islands){
            if((!island.recieved) || (!island.gave))
                checked = false;
        }
        return checked;
    }

    /**
     * Sets the migration status for all islands to false.
     */
    public void resetMigrationStatus() {
        for (Island island: islands){
            island.gave = false;
            island.recieved = false;
        }
    }

    /**
     * Integrates the migrants of each island.
     * Also asserts that the population of each island remains the same
     */
    public void integrateAllMigrants() {
        for (Island island: islands)
            island.integrateMigrants();
    }

    /**
     * Checks if all islands in the archipelago have converged
     * @return: true if all islands have converged.
     */
    public boolean checkConvergence() {
        for (Island island: islands) {
            if (!island.checkConvergence(epoch))
                return false;
        }
        return true;
    }

    /**
     * Updates the history of max fitness of all islands.
     */
    public void updateHistory() {
        for(Island island: islands)
            island.addMaxFitness();
    }

}
