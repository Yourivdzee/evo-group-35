import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.*;

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
            // Schaffers
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

        //    ** RECOMBINATION **     //
        List<String> recombinationStrategies = Arrays.asList(
                "simple-arith",  // no parameter needed
                "single-arith",  // no parameter needed
                "whole-arith",   // no parameter needed
                "BLX"            // needs parameter alfa
        );
        double alfa = 0;

        String recombinationStrategy = recombinationStrategies.get(0);


        //    ** MUTATION **     //
        List<String> mutationStrategies = Arrays.asList(
                "uniform",      // needs parameter mutationRate
                "non-uniform"   // needs parameter stdDeviation and Mean
        );
        double mutationRate = 0.05; // default
        double stdDeviation = -1.0; // ???
        double mean = 0;            // ???
        String mutationStrategy = mutationStrategies.get(0);

        //    ** REPRODUCTION PROBABILITY **     //
        double s = 1.5; // should be between 1 and 2


        int evals = 0;
        
        // init population
        Population population = new Population(populationSize);

        // calculate fitness
        System.out.println("Calculating fitness of initial population");
        for(Individual individual: population.population)
            individual.fitness = (double) evaluation_.evaluate(individual.genotype);

        while(evals<evaluations_limit_){
            System.out.println("------- Generation " + Integer.toString(evals) + " -------");

            // Select parents
            population.calculateLinearReproductionProbability(s);
            population.rouletteWheel();
            assert (!population.matingPool.isEmpty());

            // Apply crossover / mutation operators
            System.out.println("Making babies with recombination " + recombinationStrategy);
            population.makeBabies(recombinationStrategy,mutationRate);
            System.out.println("Made " + Integer.toString(population.offsprings.size()) + " babies");


            System.out.println("Evaluating newborns");
            for(Individual child: population.offsprings) {
                if(evals > 990)
                    System.out.println(Arrays.toString(child.genotype));
                // Check fitness of unknown fuction
                child.fitness = (double) evaluation_.evaluate(child.genotype);
            }

            evals++;

            // Select survivors
            population.replaceWorst();
        }

	}
}


