import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

public class Island {
    int id;

    Population population;

    boolean recieved;

    boolean gave;

    ArrayList<Double> maxFitnessHistory;

    ArrayList<Individual> newIndividuals;

    public Island(int id) {
        newIndividuals = new ArrayList<>();
        maxFitnessHistory = new ArrayList<>();
        recieved = false;
        gave = false;
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
                break;
            case "second_best":
                for(int i = 0; i < num ; i++)
                    island.newIndividuals.add(population.population.remove(2*i + 1));
                break;
            default:
                throw new IllegalArgumentException();
        }

        this.gave = true;
        island.recieved = true;
    }

    /**
     * Integrates the new migrants and checks that the population size remains the same.
     */
    public void integrateMigrants(){
        population.population.addAll(newIndividuals);
        newIndividuals.clear();
        assert (population.population.size() == population.populationSize);

    }

    /**
     * Updates the islands current maximum fitness
     */
    public void addMaxFitness() {
        population.sortPopulationByFitness();
        maxFitnessHistory.add(population.population.get(population.population.size()-1).fitness);
    }

    /**
     * Checks if the maximum fitness has converged for a number of generations.
     */
    public boolean checkConvergence(Integer num) {
        try {
            double maxFitness = Collections.max(maxFitnessHistory);
            int maxFitGeneration = maxFitnessHistory.indexOf(maxFitness);
            return maxFitnessHistory.size() - maxFitGeneration >= num;
        }catch (NoSuchElementException e){
            return false;
        }
    }
}
