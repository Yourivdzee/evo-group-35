import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;
import java.util.Collections;
import java.util.Comparator;

public class Population{
    Integer size;
    
    Integer age;

    ArrayList<Individual> population;
    
    ArrayList<Individual> matingPool;

    ArrayList<Individual> offsprings;

    public Population(Integer size){
        population = new ArrayList<>();

        for(int i = 0; i < size; i++){
            Individual indiv = new Individual();
            population.add(indiv);
        }
    }

    public ArrayList<Individual> selectParentsFitnessProportional(Integer num){

        return new ArrayList<>();
    }

    public ArrayList<Individual> selectParentsByRank(String strategy, double s) {
        ArrayList<Individual> selection = new ArrayList<>();

        sortPopulationByFitness();

        switch (strategy){
            case "linear":
                for (int i = 0; i < size; i++)
                    population.get(i).selection_prob = (2 - s)/size + 2*i*(s-1)/(size*(size-1));

            case "exp":
                break;
        }

        return selection;

    }

    /**
     * Calculates all relevant fitness statistics of a population, including:
     *  - Total fitness
     *  - Min fitness
     *  - Max fitness
     *  - Mean fitness
     *  (the reason why all of these are cramped up together in one function is
     *  to avoid unecessary performance issues when using more than one of the stats,
     *  since that would require performing several loops over the whole population)
     * @return: An ArrayList with 4 stats given in the order mentioned above.
     */
    private ArrayList<Double> calculateFitnessStatistics() {
        ArrayList<Double> stats = new ArrayList<>();

        double total_fitness = 0;
        double min_fitness = Double.MAX_VALUE;
        double max_fitness = 0;


        for(Individual individual: population){
            total_fitness = total_fitness + individual.fitness;

            if (individual.fitness < min_fitness)
                min_fitness = individual.fitness;

            if (individual.fitness > max_fitness)
                max_fitness = individual.fitness;
        }

        double mean_fitness = total_fitness/population.size();

        stats.add(total_fitness);
        stats.add(min_fitness);
        stats.add(max_fitness);
        stats.add(mean_fitness);

        return stats;
    }


    /**
     * Calculates the standard deviation of the population's fitness.
     * @return
     */
    private double calculateStandardDeviation() {
        ArrayList<Double> stats = calculateFitnessStatistics();
        double mean = stats.get(3);

        double sum = 0;
        for (Individual individual: population){
            sum = sum + Math.pow(individual.fitness - mean, 2);
        }

        double std_deviation = Math.sqrt(sum/population.size());

        return std_deviation;
    }

    public void sortPopulationByFitness(){
        Collections.sort(population, (i1, i2) -> Double.compare(i1.fitness, i2.fitness));
        return;
    }

    public ArrayList<Individual> makeChildren(ArrayList<Individual> selection){
        return new ArrayList<>();
    }


    public void evaluatePopulation(){
        return;
    }
    




    public void createMatingPool(){
        return;
    }


    public void updateGeneration(){
        return;
    }
    
}