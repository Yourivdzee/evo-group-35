import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;
import java.util.Arrays;

public class Individual{

    double[] fenotype;

    double[] genotype;

    double[] sigmas = {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1}; // small inital values for stability

    double fitness = 0.0;

    double selection_prob = 0.0;

    Random rand = new Random();

    int genosize = 10;

    double[][] alfas = new double[genosize][genosize]; //initial alfas to zeros
    double[] sigmasSqrd = new double[genosize];
    double[][] new_alfas = new double[genosize][genosize];
    double[] new_sigmas = new double[genosize];
    double[][] covariance = new double[genosize][genosize];

    // Cholesky cholesky = new Cholesky();

    /**
     * Initializes the individual with a random array of 10 doubles within 
     * the interval [-5,5]
     */
    public Individual(){
        double rand_genotype[] = new double[genosize];

        double rangeMin = -5;
        double rangeMax = 5;

        double rand_sigmas[] = new double[genosize];

        for(int i = 0; i < rand_genotype.length; i++){
            rand_genotype[i] = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();

        }

        genotype = rand_genotype;
    }


    /**
     * Initializes the individual with a given fenotype.
     * @param fenotype: the fenotype that the individual will have;
     */
    public Individual(double[] fenotype){

        // Basic represention of solution
        this.fenotype = fenotype;

        // Advanced representation of solution
        this.genotype = fenotype;

    }

    /**
     * Initializes the individual with a specific seed, mainly for testing.
     * @param seed: the seed to initialize the individual with.
     */
    public Individual(int seed){
        rand = new Random(seed);

        double rand_genotype[] = new double[genosize];

        double rangeMin = -5;
        double rangeMax = 5;

        for(int i = 0; i < rand_genotype.length; i++){
            rand_genotype[i] = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
        }

        genotype = rand_genotype;

    }

    /**
     * Mates this individual with another using one of the possible recombination strategies.
     * @param mate: The individual to mate with
     * @param recombStrategy: The recombination strategy to be used. Available options are:
     *                - "simple-arith" : Simple Arithmetic Recombination
     *                - "single-arith" : Single Arithmetic Recombination
     *                - "whole-arith" : Whole Arithmetic Recombination
     *                - "BLX" : Blend Crossover
     * @return: ArrayList of --> 2 <-- offsprings
     */
    public ArrayList<Individual> mate(Individual mate, String recombStrategy) {
        ArrayList<Individual> offsprings = new ArrayList<>();

        double[] offspringGenotype1 = new double[genotype.length];
        double[] offspringGenotype2 = new double[genotype.length];

        double alfa = 0.5; // need to find a way of encapsulating this, since it is only used for whole arithmetic recombination

        switch (recombStrategy){
            case "simple-arith":
                simpleArithmeticRecombination(mate, offspringGenotype1, offspringGenotype2);
                break;
            case "single-arith":
                singleArithmeticRecombination(mate, offspringGenotype1, offspringGenotype2);
                break;
            case "whole-arith":
                wholeArithmeticRecombination(mate, offspringGenotype1, offspringGenotype2, alfa);
                break;
            case "BLX":
                blendCrossover(mate, offspringGenotype1, offspringGenotype2, alfa);
        }


        offsprings.add(new Individual(offspringGenotype1));
        offsprings.add(new Individual(offspringGenotype2));

        return offsprings;

    }

    /**
     * Same function as mate but for BLX or crossover recombination strategy.
     */
    public ArrayList<Individual> mate(Individual mate, String recombStrategy, double alfa) {
        ArrayList<Individual> offsprings = new ArrayList<>();

        double[] offspringGenotype1 = new double[genotype.length];
        double[] offspringGenotype2 = new double[genotype.length];

        switch (recombStrategy) {
            case "BLX":
                blendCrossover(mate, offspringGenotype1, offspringGenotype2, alfa);
                break;
            case "whole-arith":
                wholeArithmeticRecombination(mate,offspringGenotype1,offspringGenotype2,alfa);
        }

        offsprings.add(new Individual(offspringGenotype1));
        offsprings.add(new Individual(offspringGenotype2));

        return offsprings;
    }

    /**
     * Mates two different individuals, resulting in two offsprings.
     * This recombination method starts by picking a random value k, takes the first
     * k genes from the parents, and averages the rest. The difference between the offsprings
     * comes from whose parents' genes are used in the first part.
     * @param mate: The partner with who mating will occur;
     * @return an ArrayList of the offsprings
     */
    public void simpleArithmeticRecombination(Individual mate, double[] genotype1, double[] genotype2){
        int k = rand.nextInt(10);

        for (int i = 0; i < genotype.length ; i++){
            if(i < k) {
                genotype1[i] = genotype[i];        // First offspring
                genotype2[i] = mate.genotype[i];   // Second offspring
            } else {
                double average = (genotype[i] + mate.genotype[i]) / 2;
                genotype1[i] = average;
                genotype2[i] = average;
            }
        }
    }

    /**
     * Mates two different individuals, resulting in two offsprings.
     * This recombination method starts by picking a random value k which corresponds to the position
     * of the gene that will be averaged. The rest of the genotype is kept from one of the parents.
     * The difference between the offsprings comes from whose parents' genes are used.
     * @param mate: The partner with who mating will occur;
     * @return an ArrayList of the offsprings
     */
    public void singleArithmeticRecombination(Individual mate, double[] genotype1, double[] genotype2){
        int k = rand.nextInt(10);

        for (int i = 0; i < genotype.length ; i++){
            if(i == k) {
                double average = (genotype[i] + mate.genotype[i])/2;
                genotype1[i] = average;        // First offspring
                genotype2[i] = average;        // Second offspring
            } else {
                genotype1[i] = genotype[i];
                genotype2[i] = mate.genotype[i];
            }
        }

    }

    /**
     * Mates two different individuals, resulting in two offsprings.
     * This recombination method takes a weighted sum of both parents using a parameter alfa.
     * @param mate: The partner with who mating will occur;
     * @param alfa; the alfa value to be used
     * @return an ArrayList of the offsprings
     */
    public void wholeArithmeticRecombination(Individual mate, double[] genotype1, double[] genotype2, double alfa) {
        for (int i = 0; i < genotype.length ; i++){
            genotype1[i] = alfa * genotype[i] + (1 - alfa) * mate.genotype[i];  // First offspring
            genotype2[i] = alfa * mate.genotype[i] + (1 - alfa) * genotype[i];  // Second offspring
        }
    }

    /**
     *
     * @param mate
     * @param genotype1
     * @param genotype2
     * @param alfa
     */
    public void blendCrossover(Individual mate, double[] genotype1, double[] genotype2, double alfa) {
        for (int i = 0; i < genotype.length ; i++){

            // init genotypes outside of bounds [-5, 5]
            genotype1[i] = 100;
            genotype2[i] = 100;

            // repeat process until the genotypes are within bounds
            while(Math.abs(genotype1[i]) > 5 || Math.abs(genotype2[i]) > 5){

                double gen_min = Math.min(genotype[i], mate.genotype[i]);
                double gen_max = Math.max(genotype[i], mate.genotype[i]);

                double d = gen_max - gen_min;

                genotype1[i] = gen_min - alfa*d + (gen_max - gen_min + 2*alfa*d)*rand.nextDouble();
                genotype2[i] = gen_min - alfa*d + (gen_max - gen_min + 2*alfa*d)*rand.nextDouble();
            }
        }
    }

    /**
     * Mutates the current individual's genome with a uniform mutation.
     * Uniform mutation randomly draws a value from [-5,5]
     */
    public void uniformMutate(double mutationRate){
        double rangeMin = -5;
        double rangeMax = 5;

        // Uniform mutation
        for (int i = 0; i < genotype.length; i++) {
            if (rand.nextDouble() < mutationRate)
                genotype[i] = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
        }
    }


    /**
     *  Mutates the current individual's genome with a non uniform mutation.
     *  Non-uniform mutation adds a small value to each gene drawn randomly from a gaussian/cauchy distribution.
     */
    public void nonUniformMutate(double stdDeviation, double mean) {
        double rangeMin = -5;
        double rangeMax = 5;

        for (int i = 0; i < genotype.length; i++) {
            genotype[i] = genotype[i] + (rand.nextGaussian() * stdDeviation + mean);
            while (genotype[i] > rangeMax || genotype[i] < rangeMin){
                genotype[i] = genotype[i] + (rand.nextGaussian() * stdDeviation + mean);
            }
        }
    }


    /**
     *  Mutates the current individual's genome with an uncorrelated n-steps mutation
     */
    public void uncorrelatedMutationNSteps(double t, double t_prime, double e) {

        double general_draw = rand.nextGaussian();

        for (int i = 0; i < genotype.length; i++) {

            double mutation = 100.0;

            while (Math.abs(genotype[i] + mutation) > 5){

                double specific_draw = rand.nextGaussian();

                sigmas[i] = Math.max(sigmas[i] * Math.exp(t_prime * general_draw + t * specific_draw), e);

                mutation = sigmas[i] * specific_draw;

            }
            genotype[i] = genotype[i] + mutation;
        }

    }


    /**
     *  Mutates the current individual's genome with a correlated muation
     */
    public void correlatedMutation(double t, double t_prime, double e, double pi_angles, double b){

        double general_draw = rand.nextGaussian();
        double[] drawVector = new double[genosize];
        double[] mutationVector = new double[genosize];

        boolean cont = true;

        // do until x + mutation is within [-5,5]
        while(cont){

            // candidate values for sigmas and alfas, will merge with regular ones if the mutation results in a individual within bounds
            double[][] new_alfas = new double[genosize][genosize];
            double[] new_sigmas = new double[genosize];

            // update sigmas
            for(int i=0; i < genosize; i++){

                double specific_draw = rand.nextGaussian();
                drawVector[i] = rand.nextGaussian();

                new_sigmas[i] = Math.max(sigmas[i] * Math.exp(t_prime * general_draw + t * specific_draw), e);

            }

            //update alfas
            for(int i=0; i < genosize; i++){
                for(int j=0; j < i; j++){

                    new_alfas[i][j] = alfas[i][j] + b * rand.nextGaussian();

                    if(Math.abs(new_alfas[i][j]) > pi_angles){
                        new_alfas[i][j] = new_alfas[i][j] - 2*pi_angles*Math.signum(new_alfas[i][j]);
                    }
                }
            }

            // calculate covariance matrix
            for(int i=0; i < genosize; i++){
                sigmasSqrd[i] = Math.pow(new_sigmas[i], 2);
                covariance[i][i] = sigmasSqrd[i];

                for(int j=0; j < i; j++){
                    covariance[i][j] = (sigmasSqrd[i] - sigmasSqrd[j])*Math.tan(Math.toRadians(2*new_alfas[i][j]))/2;
                    covariance[j][i] = covariance[i][j];
                }
            }

            // calculate mutation vectror L x N(0,1), where L is the left matrix from cholesky decomposition of the covariance matrix
            // double[][] leftChol = cholesky.cholesky(covariance);
            mutationVector = matrixMult(covariance, drawVector);

            // check if every gene is within bounds. if there is at least one outside of [-5,5] repeat the process
            for(int i=0; i < genosize; i++){
                if(Math.abs(genotype[i] + mutationVector[i]) > 5){
                    break;
                }
                cont = false;
            }
        }

        // update genotype, sigmas and alfas
        for(int i=0; i < genosize; i++){
            genotype[i] = genotype[i] + mutationVector[i];
        }
        alfas = new_alfas;
        sigmas = new_sigmas;

    }


    // Matrix mul between 2-dimensional a and 1-dimensional b
    public double[] matrixMult(double[][] a, double[] b){
        double [] c = new double[genosize];

        for(int i=0; i < genosize; i++){
            for(int j=0; j < genosize; j++){
                c[i] += a[i][j]*b[j];
            }
        }

        return c;
    }


    /**
     * Determines the genotype, based on the fenotype which was given as input (an array of doubles).
     * This function will be used in case many different representations of genotype are used, besides the array of doubles.
     * @return
     */
    public double[] determineGenotype(){
        return genotype;
    }



    
}