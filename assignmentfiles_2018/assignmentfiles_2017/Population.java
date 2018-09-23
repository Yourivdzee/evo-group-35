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
        System.out.println("Initializing population of size " + Integer.toString(size));
        population = new ArrayList<>();

        this.size = size;

        matingPool = new ArrayList<Individual>();
        offsprings = new ArrayList<Individual>();

        for(int i = 0; i < size; i++){
            Individual indiv = new Individual();
            population.add(indiv);
        }
    }

    /**
     * Calculates and assigns the reproduction probability of each individual based
     * on a linear model.
     * @param s: parametrization values, should be 1<s<=2
     */
    public void calculateLinearReproductionProbability(double s) {
        sortPopulationByFitness();
        for(int i = 0; i < size; i++){
            population.get(i).selection_prob = (2 - s) / size + 2 * i * (s - 1) / (size * (size - 1));
        }
    }


    /**
     * Calculates and assigns the reproduction probability of each individual based
     * on an exponential model.
     */
    public void calculateExponentialReproductionProbability( ){
        sortPopulationByFitness();
        ArrayList<Double> exponentialFactiors = new ArrayList<>();
        double sum = 0.0;

        for (int i = 0; i < size; i++) {
            double factor = (1 - Math.pow(Math.E, -i));
            exponentialFactiors.add(factor);
            sum += factor;
        }

        double c = sum;

        for (int i = 0; i < size; i++) {
            population.get(i).selection_prob = exponentialFactiors.get(i)/c;
        }

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
    public ArrayList<Double> calculateFitnessStatistics() {
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
    public ArrayList<Double> calculateCumulativeReproductionProbability () {
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
        System.out.println("Starting roulette wheel selection");

        int currentMember = 0;
        Individual[] result = new Individual[size];

        ArrayList<Double> cumulativeProbability = calculateCumulativeReproductionProbability();

        assert (cumulativeProbability.get(cumulativeProbability.size() - 1) == 1.0);

        while (currentMember < size) {
            double r = rand.nextDouble();
            int i = 0;
            while (cumulativeProbability.get(i) < r)
                i++;
            result[currentMember] =  population.get(i);
            currentMember++;
        }

        Collections.addAll(matingPool, result);

    }

    /**
     * Fills up the mating pool using the stochastic universal sampling selection method.
     * Algorithm taken from the book.
     */
    public void stochasticUniversalSampling() {
        int currentMember = 0;
        int i = 0;
        int matingPoolSize = size;
        Individual[] result = new Individual[size];

        ArrayList<Double> cumulativeProbability = calculateCumulativeReproductionProbability();

        double r = rand.nextDouble() * 1./matingPoolSize;

        while (currentMember < matingPoolSize) {
            while ( r < cumulativeProbability.get(i)){
                result[currentMember] = population.get(i);
                r = r + 1./matingPoolSize;
                currentMember++;
            }
            i++;
        }

        Collections.addAll(matingPool, result);
    }



    /**
     * Calculates the standard deviation of the population's fitness.
     * @return
     */
    public double calculateStandardDeviation() {
        ArrayList<Double> stats = calculateFitnessStatistics();
        double mean = stats.get(3);

        double sum = 0;
        for (Individual individual: population){
            sum = sum + Math.pow(individual.fitness - mean, 2);
        }

        double std_deviation = Math.sqrt(sum/population.size());

        return std_deviation;
    }

    /**
     * Sorts the population by fitness.
     */
    public void sortPopulationByFitness(){
        Collections.sort(population, (i1, i2) -> Double.compare(i1.fitness, i2.fitness));
    }

    /**
     * Replaces the current population by the current offsprings following one of two strategies:
     *  1. All parents are replaced by the offsprings. The new generation is ENTIRELY new.
     *  2. When there are less offsprings than parents, part of the new population is compos
     */
    public void replacePopulationByAge() {
        if (offsprings.size() == population.size()){
            population.clear();
            population.addAll(offsprings);
            offsprings.clear();

        } else if (offsprings.size() < population.size() ) {
            population.clear();
            population.addAll(offsprings);
            offsprings.clear();

            while (population.size() < size)
                population.add(matingPool.get(rand.nextInt(matingPool.size())));

            assert (population.size() == size);

        } else {
            System.out.println("Currently have " + Double.toString(offsprings.size()) + " offsprings and " + Double.toString(population.size()) + " population. Did not expect to have a higer offspring count");
        }
    }

    /**
     * Replaces the individuals with less fitness of a population
     */
    public void replaceWorst() {
        sortPopulationByFitness();
        while (offsprings.size() > 0){
            population.remove(0);
            Individual offspring = offsprings.remove(0);
            population.add(offspring);
        }
    }

    /**
     * In this selection strategy the parents and offsprings are all joined together
     * and the ones with top fitness are the survivors.
     */
    public void muPlusLambdaSelection() {
        ArrayList<Individual> result = new ArrayList<Individual>();
        result.addAll(offsprings);
        result.addAll(matingPool);
        Collections.sort(result, (i1, i2) -> Double.compare(i1.fitness, i2.fitness));

        result.subList(result.size() - size, result.size());

        population.clear();
        population.addAll(result);
        offsprings.clear();
    }

    /**
     * Selection when there are more offsprings than population size.
     * ALl offsprings are added to the current generation, and then the ones
     * with lower fitness are progressively removed until the generation has the expected size.
     */
    public void muCommaLambdaSelection() {
        ArrayList<Individual> result = new ArrayList<Individual>();
        result.addAll(offsprings);
        Collections.sort(result, (i1, i2) -> Double.compare(i1.fitness, i2.fitness));
        while (result.size() > size)
            result.remove(0);

    }

    /**
     *  Tournament selection of survivors. The mating pool and the offsprings are all
     *  joined into one list. Each of them 'fights' 10 random peers and depending on the amount
     *  of wins, is put in a specific position.
     *  After the tournament, the top winners are added to the list of survivors until the
     *  current population reaches the expected size.
     */
    public void roundRobinTournamentSelection() {
        ArrayList<Individual> tournamentPool = new ArrayList<Individual>();
        tournamentPool.addAll(offsprings);
        tournamentPool.addAll(matingPool);
        int q = 10; // recommended

        HashMap<Integer, ArrayList<Individual>> winCounts = new HashMap<>();
        for(int i = 0; i <= q; i++){
            winCounts.put(i, new ArrayList<Individual>());
        }

        for(Individual individual: tournamentPool){
            tournamentPool.remove(individual);
            int wins = 0;
            for(int i = 0; i <= q; i++){
                Individual opponent = tournamentPool.get(rand.nextInt(tournamentPool.size()));
                if (individual.fitness > opponent.fitness)
                    wins++;
            }
            winCounts.get(wins).add(individual);
        }

        population.clear();
        int counter = q;
        while(population.size() < size){
            ArrayList<Individual> currentWinners = winCounts.get(counter);
            if(currentWinners.size() <= size)
                population.addAll(currentWinners);
            else
                population.addAll(currentWinners.subList(0,size));
        }
    }

    /**
     * Fills in the offspring arraylist with babies according to the given recombination
     * strategy. If given a mutation probability the mutation will be uniform, if given
     * a standard deviation and a mean, mutation will be non-uniform.
     * @param recombStrategy
     *
     */
    public void makeBabies (String recombStrategy, double mutationProb) {
        while (matingPool.size() > 0){
            ArrayList<Individual> parents = randomSelectMates(2);
            Individual parent = parents.get(0);
            Individual mate = parents.get(1);

            // Make babies
            ArrayList<Individual> babies = parent.mate(mate,recombStrategy);

            for(Individual baby: babies)
                baby.uniformMutate(mutationProb);

            offsprings.addAll(babies);
        }
    }

    public void makeBabies (String recombStrategy, double stdDeviation, double mean) {
        while (matingPool.size() >= 2){
            ArrayList<Individual> parents = randomSelectMates(2);
            Individual parent = parents.get(0);
            Individual mate = parents.get(1);

            // Make babies
            ArrayList<Individual> babies = parent.mate(mate,recombStrategy);

            for(Individual baby: babies)
                baby.nonUniformMutate(stdDeviation,mean);

            offsprings.addAll(babies);
        }
    }

    /**
     * Randomly selects num mates from the mating pool
     * @param num: the number of mates to select.
     * @return ArrayList<Individuals> of length num.
     */
    private ArrayList<Individual> randomSelectMates(int num) {
        ArrayList<Individual> mates = new ArrayList<>();

        for(int i = 0; i < num ; i++){
            mates.add(matingPool.remove(rand.nextInt(matingPool.size())));
        }

        return mates;
    }

}