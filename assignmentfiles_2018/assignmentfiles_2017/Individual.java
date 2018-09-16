
import java.util.ArrayList;

import java.util.concurrent.ThreadLocalRandom;

import java.lang.Math;

import java.util.Random;

public class Individual{

    double[] fenotype;

    double[] genotype;

    double fitness = 0.0;

    double selection_prob = 0.0;

    Random rand = new Random();

    int genosize = 10;

    /**
     * Initializes the individual with a random array of 10 doubles within 
     * the interval [-5,5]
     */
    public Individual(){
        double rand_genotype[] = new double[genosize];

        double rangeMin = -5;
        double rangeMax = 5;

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
     * @param strategy: The recombination strategy to be used. Available options are:
     *                - "simple-arith" : Simple Arithmetic Recombination
     *                - "single-arith" : Single Arithmetic Recombination
     *                - "whole-arith" : Whole Arithmetic Recombination
     *                - "BLX" : Blend Crossover
     * @return: ArrayList of all the offsprings
     */
    public ArrayList<Individual> mate(Individual mate, String strategy) {
        ArrayList<Individual> offsprings = new ArrayList<>();

        double[] offspringGenotype1 = new double[genotype.length];
        double[] offspringGenotype2 = new double[genotype.length];

        double alfa = 0.5; // need to find a way of encapsulating this, since it is only used for whole arithmetic recombination

        switch (strategy){
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
                break;
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
            double dist = Math.abs(genotype[i] - mate.genotype[i]); // not needed??
            double gama = (1 - 2*alfa)*rand.nextDouble() - alfa;
            genotype1[i] = (1 - gama)*genotype[i] + gama*mate.genotype[i];
            genotype2[i] = (1 - gama)*mate.genotype[i] + gama*genotype[i];
        }
    }

    /**
     * Mutates the current individual's genome with a uniform mutation.
     * Uniform mutation randomly draws a value from [-5,5]
     */
    public void uniformMutate(double mutation_prob){
        double rangeMin = -5;
        double rangeMax = 5;

        // Uniform mutation
        for (int i = 0; i < genotype.length; i++) {
            if (rand.nextDouble() < mutation_prob)
                genotype[i] = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
        }
    }


    /**
     *  Mutates the current individual's genome with a non uniform mutation.
     *  Non-uniform mutation adds a small value to each gene drawn randomly from a gaussian/cauchy distribution.
     */
    public void nonUniformMutate(double stdDeviation, double mean) {
        for (int i = 0; i < genotype.length; i++) {
            genotype[i] = genotype[i] + (rand.nextDouble()*stdDeviation + mean);

        }
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