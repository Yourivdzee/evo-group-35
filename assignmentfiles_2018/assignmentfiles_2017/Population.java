import java.util.*;
import java.lang.Math;

public class Population{

    Integer age = 0;

    ArrayList<Individual> population;

    ArrayList<Individual> matingPool;

    ArrayList<Individual> offsprings;

    ArrayList<Individual> bestIndividuals;

    Random rand = new Random();

    /* ---- PARAMETERS ---- */

    Integer populationSize;

    Integer matingPoolSize;

    Integer offspringsSize;

    String recombinationStrat;

    String mutationStrat;

    String reproductionProbabilityStrat;

    String parentSelectionStrat;

    String survivorSelectionStrat;

    Double alfa;

    Double mutationRate;

    Double stdDeviation;

    Double mean;

    Double s;

    Integer k;

    Integer q;

    public Population(Integer size, Integer matingPoolSize, Integer offspringsSize){
        population = new ArrayList<>();
        matingPool = new ArrayList<>();
        offsprings = new ArrayList<>();
        bestIndividuals = new ArrayList<>();

        this.populationSize = size;
        this.matingPoolSize = matingPoolSize;
        this.offspringsSize = offspringsSize;

        for(int i = 0; i < populationSize; i++){
            Individual indiv = new Individual();
            population.add(indiv);
        }
    }

    /**
     * Sets the reproduction probability calculation strategy.
     * @param strat: selected strategy. Possible strategies are:
     *             - "linear" (needs parameter s)
     *             - "exponential" (no paramter)
     */
    public void setReproductionProbabilityStrategy(String strat){
        this.reproductionProbabilityStrat = strat;
    }

    public void setReproductionProbabilityStrategy(String strat, double s){
        this.reproductionProbabilityStrat = strat;
        this.s = s;
    }

    /**
     * Sets the parent selection strategy.
     * @param strat: selected strategy. Possible strategies are:
     *             - Roulette wheel - "RW"
     *             - Stochastic universal sampling - "SUS"
     *             - Tournament selection - "tournament"
     */
    public void setParentSelectionStrategy(String strat){
        this.parentSelectionStrat = strat;
    }

    public void setParentSelectionStrategy(String strat, Integer k){
        this.parentSelectionStrat = strat;
        this.k = k;
    }


    /**
     * Sets the recombination strategy for this population.
     * @param strat: selected strategy. Possible strategies are:
     *             - "simple-arith" : Simple Arithmetic Recombination
     *             - "single-arith" : Single Arithmetic Recombination
     *             - "whole-arith" : Whole Arithmetic Recombination
     *             - "BLX" : Blend Crossover (needs parameter alfa)
     */
    public void setRecombinationStrategy(String strat, double alfa){
        this.recombinationStrat = strat;
        this.alfa = alfa;
    }

    public void setRecombinationStrategy(String strat){
        this.recombinationStrat = strat;
    }

    /**
     * Sets the mutation strategy for this population.
     * @param strat: selected strategy. Possible strategies are:
     *             - "Uniform" (needs mutation rate paramater)
     *             - "Non-uniform" (needs stdDeviation and mean parameter)
     */
    public void setMutationStrategy(String strat, double mutationRate){
        this.mutationStrat = strat;
        this.mutationRate = mutationRate;
    }

    public void setMutationStrategy(String strat, double stdDeviation, double mean){
        this.mutationStrat = strat;
        this.stdDeviation = stdDeviation;
        this.mean = mean;
    }

    /**
     * Sets the survivor selection strategy for this population,
     * @param strat: selected strategy. Possible strategies are:
     *             - "tournament" - round-robin tournament (needs parameter q)
     *             - "mu,lambda" - (mu,lambda)
     *             - "mu+lambda" - (mu+lambda)
     *             - "replaceWorst" - replace worst strategy
     *             - "age" - age based survivor selection
     */
    public void setSurvivorSelectionStrategy(String strat){
        this.survivorSelectionStrat = strat;
    }

    public void setSurvivorSelectionStrategy(String strat, Integer q){
        this.survivorSelectionStrat = strat;
        this.q = q;
    }

    /**
     * Calculates all relevant fitness statistics of a population, including:
     *  - Min fitness
     *  - Max fitness
     *  - Mean fitness
     *  - Std fitness
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
        double std_fitness = 0;


        for(Individual individual: population){
            total_fitness = total_fitness + individual.fitness;

            if (individual.fitness < min_fitness)
                min_fitness = individual.fitness;

            if (individual.fitness > max_fitness)
                max_fitness = individual.fitness;
        }

        double mean_fitness = total_fitness/population.size();

        for(Individual individual: population){
            std_fitness += Math.pow(individual.fitness - mean_fitness, 2);
        }

        std_fitness = Math.sqrt(std_fitness / population.size());

        stats.add(min_fitness);
        stats.add(max_fitness);
        stats.add(mean_fitness);
        stats.add(std_fitness);

        return stats;
    }

    /**
     * Calculates and assigns the reproduction probability of each individual based
     * on a linear model.
     * @param s: parametrization values, should be 1<s<=2
     */
    public void calculateLinearReproductionProbability(double s) {
        sortPopulationByFitness();
        for(int i = 0; i < populationSize; i++){
            population.get(i).selection_prob = (2 - s) / populationSize + 2 * i * (s - 1) / (populationSize * (populationSize - 1));
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

        for (int i = 0; i < populationSize; i++) {
            double factor = (1 - Math.pow(Math.E, -i));
            exponentialFactiors.add(factor);
            sum += factor;
        }

        double c = sum;

        for (int i = 0; i < populationSize; i++) {
            population.get(i).selection_prob = exponentialFactiors.get(i)/c;
        }

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
        Individual[] result = new Individual[matingPoolSize];

        ArrayList<Double> cumulativeProbability = calculateCumulativeReproductionProbability();

        assert (cumulativeProbability.get(cumulativeProbability.size() - 1) == 1.0);

        while (currentMember < matingPoolSize) {
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
        Individual[] result = new Individual[matingPoolSize];

        ArrayList<Double> cumulativeProbability = calculateCumulativeReproductionProbability();

        double r = rand.nextDouble() /matingPoolSize;

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

    public void tournamentSelection() {
        int currentMember = 0;

        while (currentMember < matingPoolSize){
            ArrayList<Individual> randSelection = new ArrayList<>();
            for(int i = 0 ; i < k ; i++){
                randSelection.add(population.get(rand.nextInt(population.size() - 1)));
            }

            Collections.sort(randSelection, (i1, i2) -> Double.compare(i1.fitness, i2.fitness));

            Individual i = randSelection.get(randSelection.size() - 1);


            matingPool.add(i);
            currentMember += 1;
        }
    }

    /**
     * Sorts the population by fitness.
     */
    public void sortPopulationByFitness(){
        Collections.sort(population, (i1, i2) -> Double.compare(i1.fitness, i2.fitness));
    }


    /**
     * Stores the most fit individuals
     * @param num: how many individuals to store.
     */
    public void storeBestIndividuals(int num) {
        sortPopulationByFitness();
        for (int i = 0 ; i < num; i++)
            bestIndividuals.add(population.get(population.size()- 1 - i));
    }

    /**
     * Reinserts the best individuals into the population by the removing the worst
     * newborns.
     */
    public void reinsertBestIndividuals() {
        sortPopulationByFitness();
        for (int i = 0; i < bestIndividuals.size() ; i++){
            Individual newborn = population.get(0);
            if(newborn.fitness < bestIndividuals.get(i).fitness) {
                population.remove(newborn);
                population.add(bestIndividuals.get(i));
            }
        }
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

            while (population.size() < populationSize)
                population.add(matingPool.get(rand.nextInt(matingPool.size())));

            assert (population.size() == populationSize);

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

        assert (population.size() == populationSize);
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

        result.subList(result.size() - populationSize, result.size());

        population.clear();
        population.addAll(result);
        offsprings.clear();

        assert (population.size() == populationSize);
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
        while (result.size() > populationSize)
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

        HashMap<Integer, ArrayList<Individual>> winCounts = new HashMap<>();
        for(int i = 0; i < q; i++){
            winCounts.put(i, new ArrayList<Individual>());
        }

        for(int j = 0; j < tournamentPool.size() ; j++){
            Individual individual = tournamentPool.get(j);
            int wins = 0;
            for(int i = 0; i < q; i++){
                Individual opponent = tournamentPool.get(rand.nextInt(tournamentPool.size()));
                if (individual.fitness > opponent.fitness)
                    wins++;
            }
            winCounts.get(wins).add(individual);
        }

        population.clear();
        int counter = q - 1;
        while(population.size() < populationSize){
            ArrayList<Individual> currentWinners = winCounts.get(counter);
            if(currentWinners.size() <= populationSize)
                population.addAll(currentWinners);
            else
                population.addAll(currentWinners.subList(0,populationSize));

            counter--;
        }

        assert (population.size() == populationSize);
    }

    /**
     * Fills in the offspring arraylist with babies according to the given recombination
     * strategy. If given a mutation probability the mutation will be uniform, if given
     * a standard deviation and a mean, mutation will be non-uniform.
     * ATTENTION: same individual mating is made possible
     */
    public void makeBabies () {
        while (matingPool.size() > 0){

            ArrayList<Individual> parents = randomSelectMates(2);
            Individual parent = parents.get(0);
            Individual mate = parents.get(1);


            // Make babies
            ArrayList<Individual> babies = parent.mate(mate,recombinationStrat);

            if (mutationStrat.equals("uniform"))
                mutate(babies,this.mutationRate);
            else
                mutate(babies,this.stdDeviation, this.mean);

            offsprings.addAll(babies);
        }
    }

    /**
     * Mutates the list of babies with the adequate mutation strategy
     * @param babies List of individuals to mutate
     * @param mutationRate Uniform mutation rate parameter
     */
    public void mutate(ArrayList<Individual> babies, double mutationRate) {
        for(Individual baby: babies)
            baby.uniformMutate(mutationRate);
    }

    /**
     * Mutates the list of babies with the adequate mutation strategy
     * @param babies List of individuals to mutate
     * @param stdDeviation No mutation rate parameter standard deviation
     * @param mean No mutation rate parameter mean
     */
    public void mutate(ArrayList<Individual> babies, double stdDeviation, double mean) {
        for(Individual baby: babies)
            baby.nonUniformMutate(stdDeviation,mean);
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

    /**
     * Executes parent selection with the chosen reproduction probability
     * calculation and the parent selection strategy.
     */
    public void selectParents() {
        // Calculate reproduction probability
        switch (reproductionProbabilityStrat) {
            case "linear":
                calculateLinearReproductionProbability(this.s);
                break;
            case "exponential":
                this.calculateExponentialReproductionProbability();
                break;
            default:
                throw new IllegalArgumentException();
        }

        // Execute parent selection
        switch (parentSelectionStrat) {
            case "RW":
                rouletteWheel();
                break;
            case "SUS":
                stochasticUniversalSampling();
                break;
            case "tournament":
                tournamentSelection();
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Selects survivors based on the survivor selection strategy
     */
    public void selectSurvivors() {
        age++;
        switch (survivorSelectionStrat) {
            case "tournament":
                roundRobinTournamentSelection();
                break;
            case "mu,lambda":
                muCommaLambdaSelection();
                break;
            case "mu+lambda":
                muPlusLambdaSelection();
                break;
            case "replaceWorst":
                replaceWorst();
                break;
            case "age":
                replacePopulationByAge();
                break;
            default:
                throw new IllegalArgumentException();
        }
        matingPool.clear();

    }

    /**
     * Evolves the population by one generation.
     */
    public void evolve() {
        // Select parents
        this.selectParents();
        assert (!this.matingPool.isEmpty());


        // Apply crossover / mutation operators
        this.makeBabies();
    }

}