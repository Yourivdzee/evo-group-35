import java.util.ArrayList;
import java.util.Random;

public class Archipelago {
    int size;

    int epoch; // 25-150 recommended

    int numExchangeIndividuals; // 2-5 recommended

    Random rand;

    ArrayList<Island> islands;

    ArrayList<Island> migrationPool;

    public Archipelago(Integer size) {
        this.size = size;
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
                for(int i = 1; i < islands.size() + 1 ; i++){
                    int current = i%islands.size();
                    int next = i+1%islands.size();
                    islands.get(current).give(islands.get(next), numExchangeIndividuals, "whatever");
                }
            case "star":
                for (Island island: islands ) {
                    Island randomIsland = islands.get(rand.nextInt(islands.size()));
                    while (randomIsland.recieved) {
                        randomIsland = islands.get(rand.nextInt(islands.size()));
                    }
                    island.give(randomIsland, numExchangeIndividuals, "whatever");
                }
        }

        assert checkMigrationStatus();

    }

    /**
     * Checks that all islands have given and recieved individuals
     * Used as an assertion.
     * @return: true if all islands have given/recieved individuals.
     */
    private boolean checkMigrationStatus() {
        boolean checked = true;
        for (Island island: islands){
            if((!island.recieved) || (!island.gave))
                checked = false;
        }
        return checked;
    }

    /**
     * Integrates the migrants of each island.
     * Also asserts that the population of each island remains the same
     */
    public void integrateAllMigrants() {
        for (Island island: islands)
            island.integrateMigrants();
    }


}
