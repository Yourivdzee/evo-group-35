import java.util.*;
import java.lang.Math;

public class Population{
    Integer size;
    
    Integer age;

    ArrayList<Individual> population;
    
    ArrayList<Individual> matingPool;

    ArrayList<Individual> offsprings;

    Random rand = new Random();

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

    /**
     * Calculates and assigns the reproduction probability of each individual based
     * on a linear model.
     * @param s: parametrization values, should be 1<s<=2
     */
    public void calculateLinearReproductionProbability(double s){
        for (int i = 0; i < size; i++)
            population.get(i).selection_prob = (2 - s)/size + 2*i*(s-1)/(size*(size-1));
    }


    /**
     * Calculates and assigns the reproduction probability of each individual based
     * on an exponential model.
     * @param c: normalisation factor, should be chosen so that the sum of probabilities
     * is 1.
     */
    public void calculateExponentialReproductionProbability(double c){
        for (int i = 0; i < size; i++)
            population.get(i).selection_prob = (1- Math.pow(Math.E,-i))/c;
    }

    public ArrayList<Individual> selectParentsByRank(String strategy, double s) {
        ArrayList<Individual> selection = new ArrayList<>();

        sortPopulationByFitness();



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
     * Calculates the cumulative reproduction probability of a population.
     * Must be used after calculating the reproduction probabilities of each individual.
     * @return ArrayList of doubles in which each index corresponds to the index in the
     * population array.
     */
    private ArrayList<Double> calculateCumulativeReproductionProbability () {
        ArrayList<Double> cumulativeProbability = new ArrayList<Double>();

        double acc = 0;

        try {
            for (Individual individual : population) {
                acc += individual.selection_prob;
                cumulativeProbability.add(acc);
            }
        } catch (NullPointerException e) {
            System.out.println("Please calculate the selection probability of the individuals before trying to obtain the cumulative reproduction probability distribution.");
        }

        assert (Math.abs(acc - 1) < 0.00001);

        return cumulativeProbability;
    }

    /**
     * Fills up the mating pool using the roulette wheel selection method.
     * ALgorithm taken from the book.
     */
    public void rouletteWheel() {
        int currentMember = 1;

        ArrayList<Double> cumulativeProbability = calculateCumulativeReproductionProbability();

        while (currentMember < size) {
            double r = rand.nextGaussian();
            int i = 1;
            while (cumulativeProbability.get(i) < r)
                i++;
            matingPool.set(currentMember, population.get(i));
        }

    }

    /**
     * Fills up the mating pool using the stochastic universal sampling selection method.
     * Algorithm taken from the book.
     */
    public void stochasticUniversalSampling() {
        int currentMember = 1;
        int i = 1;
        int matingPoolSize = size;

        ArrayList<Double> cumulativeProbability = calculateCumulativeReproductionProbability();

        double r = rand.nextGaussian() * 1/matingPoolSize;

        while (currentMember < matingPoolSize) {
            while ( r < cumulativeProbability.get(i)){
                matingPool.set(currentMember, population.get(i));
                r = r + 1./matingPoolSize;
                currentMember++;
            }
            i++;
        }
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