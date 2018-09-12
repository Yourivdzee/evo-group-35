
import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.concurrent.ThreadLocalRandom;

public class Individual{

    double[] fenotype;

    double[] genotype;

    double fitness;

    /**
     * Initializes the individual with a random array of 10 doubles within 
     * the interval [-5,5]
     */
    public Individual(){
        double rand_genotype[] = new double[10];

        for(int i = 0; i < rand_genotype.length; i++){
            rand_genotype[i] = ThreadLocalRandom.current().nextInt(-5, 5 + 1);
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

        this.fitness = 0.0;
    }

    public ArrayList<Individual> mate(Individual mate){
        return new ArrayList<Individual>();
    }

    public void mutate(){
        System.out.print("MUTATING");
    }

    public ArrayList<Individual> mate(ArrayList<Individual> mates){
        return new ArrayList<Individual>();
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