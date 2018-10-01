import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Group35 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;

	public Group35()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));
        // BentCigar: not multimodal, not structured, not seperable
        // Katsuura: is multimodal, not structured, not seperable
        // Schaffers: is multimodal, is structured, not seperable


        // Do sth with property values, e.g. specify relevant settings of your algorithm
        if (!isMultimodal && !hasStructure) {
            // BentCigar
            // Do sth
        } else if (isMultimodal && !hasStructure) {
            // Katsuura
            // Do sth else
        } else if (isMultimodal && hasStructure) {
            // Schaffers]
            // Do sth else
        } else {
            // Testfunction
        }


    }



	public void run()
	{
		// Run your algorithm here
        System.out.println("Initializing algorithm...");


        // ------- PARAMETERS ------- //
        //      ** GENERAL **         //
        int populationSize = 10;
        int offspringsSize = 10;
        int matingPoolSize = offspringsSize;

        //    ** RECOMBINATION **     //
        List<String> recombinationStrategies = Arrays.asList(
                "simple-arith",  // no parameter needed
                "single-arith",  // no parameter needed
                "whole-arith",   // no parameter needed
                "BLX"            // needs parameter alfa
        );
        double alfa = 0.5;

        String recombinationStrategy = recombinationStrategies.get(0);


        //    ** MUTATION **     //
        List<String> mutationStrategies = Arrays.asList(
                "uniform",      // needs parameter mutationRate
                "non-uniform"   // needs parameter stdDeviation and Mean
        );
        double mutationRate = 0.05; // default
        double stdDeviation = 0.2; // ???
        double mean = 0;            // ???
        String mutationStrategy = mutationStrategies.get(1);

        //    ** REPRODUCTION PROBABILITY **     //
        List<String> reproductionProbabilityStrategies = Arrays.asList(
                "linear",       // needs parameter s
                "exponential"   // no parameter needed
        );
        double s = 1.5; // should be between 1 and 2


        int evals = 0;
        
        // init population
        System.out.println("Initializing population - μ: " + Integer.toString(populationSize) + "  λ:" + Integer.toString(offspringsSize));

        Population population = new Population(populationSize, matingPoolSize, offspringsSize);
        population.setReproductionProbabilityStrategy("linear", s);
        population.setParentSelectionStrategy("SUS");
        population.setRecombinationStrategy("simple-arith");
        population.setMutationStrategy("uniform", 0.05);
        population.setSurvivorSelectionStrategy("replaceWorst");

        // calculate fitness
        System.out.println("Calculating fitness of initial population");
        for(Individual individual: population.population)
            individual.fitness = (double) evaluation_.evaluate(individual.genotype);

        while(evals<evaluations_limit_){
            System.out.println("------- Generation " + Integer.toString(evals) + " -------");

            // Select parents
            population.selectParents();
            assert (!population.matingPool.isEmpty());
            System.out.println("Finished parent selection with " + Integer.toString(population.matingPool.size()) + " candidates.");

            // Apply crossover / mutation operators
            System.out.println("Making babies with recombination " + recombinationStrategy);
            population.makeBabies();
            System.out.println("Made " + Integer.toString(population.offsprings.size()) + " babies");


            System.out.println("Evaluating newborns");
            for(Individual child: population.offsprings) {
                // Check fitness of unknown fuction
                child.fitness = (double) evaluation_.evaluate(child.genotype);
            }

            evals++;

            // Select survivors
            population.selectSurvivors();
        }

	}
}


