import java.util.ArrayList;
import java.util.Collections;

public class Island {
    int id;

    Population population;

    boolean recieved;

    boolean gave;

    double maxFitness;

    ArrayList<Individual> newIndividuals;

    public Island(int id) {
        newIndividuals = new ArrayList<>();
        recieved = false;
        gave = false;
        maxFitness = 0;
        this.id = id;
    }

    public void populate(Population population) {
        this.population = population;
    }


    /**
     * Migrates a number of individuals to another island
     * @param island: island to migrate to.
     * @param num: number of individuals to migrate.
     * @param strat: strategy used to migrate. Possible strategies are:
     *             - "best"
     *             - "second_best"
     *             -
     */
    public void give(Island island, Integer num, String strat){
        population.sortPopulationByFitness();
        Collections.reverse(population.population);
        switch (strat){
            case "best":
                for(int i = 0; i < num ; i++)
                    island.newIndividuals.add(population.population.remove(i));
            case "second_best":
                for(int i = 1; i < num ; i++)
                    island.newIndividuals.add(population.population.remove(i + 1));
        }
        this.gave = true;
        island.recieved = true;
    }

    /**
     * Integrates the new migrants and checks that the population size remains the same.
     */
    public void integrateMigrants(){
        population.population.addAll(newIndividuals);
        assert (population.population.size() == population.populationSize);
    }

    /**
     * Updates the islands current maximum fitness
     */
    public void updateMaxFitness() {
        population.sortPopulationByFitness();
        maxFitness = population.population.get(population.population.size()-1).fitness;
    }
}
