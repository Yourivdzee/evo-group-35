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
	String function_name;
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

    @Override
    public void run() {
        run_island();
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
           function_name = "BentCigar";
        } else if (isMultimodal && !hasStructure) {
            function_name = "Katsuura";
        } else if (isMultimodal && hasStructure) {
            function_name = "Schaffers";
        } else {
            function_name = "Sphere (Test function)";
        }


    }


    public void run_island()
    {
        // Run your algorithm here
        // System.out.println("Initializing algorithm...");

        // gather stats for population and archipelago (stats collection may slow down the process)
        boolean gather = true;

    	// Initialize printer class
    	Printing printing = new Printing();

        // ------- PARAMETERS ------- //
        //      ** GENERAL **         //
        int populationSize = 100;
        int offspringsSize = 100;
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
                "non-uniform",   // needs parameter stdDeviation and Mean
                "non-uniform-ctrl-det",
                "non-uniform-ctrl-adap"
        );
        double mutationRate = 0.05; // default
        double stdDeviation = 0.2; // ???
        double mean = 0;            // ???
        String mutationStrategy = mutationStrategies.get(3);

        //    ** REPRODUCTION PROBABILITY **     //
        List<String> reproductionProbabilityStrategies = Arrays.asList(
                "linear",       // needs parameter s
                "exponential"   // no parameter needed
        );
        double s = 1.5; // should be between 1 and 2


        int evals = 0;

        // Column names for output file
        if (gather){
	        System.out.println("Generation IslandId Exchange Maximum Average StandardDev");
		}

        int numIslands = 1;
        int numExchangeIndividuals = 2;
        int epoch = 10;
        int archipelagoSize = numIslands * populationSize;

        Archipelago archipelago = new Archipelago(numIslands,numExchangeIndividuals, epoch);

        // init population
        // System.out.println("Initializing population - μ: " + Integer.toString(populationSize) + "  λ:" + Integer.toString(offspringsSize));

        for(int i = 0; i < archipelago.size; i++){
            Population population = new Population(evaluation_, populationSize, matingPoolSize, offspringsSize);
            population.setReproductionProbabilityStrategy("linear", s);
            population.setParentSelectionStrategy("tournament", populationSize / 10);
            population.setRecombinationStrategy("simple-arith");
//            population.setMutationStrategy("Uniform", 0.05);
            population.setMutationStrategy(mutationStrategy, 0.05  , 0.0);
            population.setSurvivorSelectionStrategy("replaceWorst");
            archipelago.islands.get(i).populate(population);
        }

        // calculate fitness
        // System.out.println("Calculating fitness of initial population");

        ArrayList<Double> archipelagoFitnessValues = new ArrayList<>();
        int island_id = 0;

        for(Island island: archipelago.islands) {
            island_id++;
            Population population = island.population;
            //evals += population.evaluate();
            for (Individual individual : population.population) {
                individual.fitness = (double) evaluation_.evaluate(individual.genotype);
                System.out.println(individual.fitness);
                EvaluationCounter.increaseEvaluation();

            }

            // calculate and print initial fintess statistics for this island
            if (gather){
//	            printing.printStats(archipelago.age, island_id, false, population.calculateFitnessStatistics());
            }

        // calculate and print initial statistics for the archipelago
        if(gather){
        	printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics());
        }

        }
        //evaluations_limit_
        while(EvaluationCounter.getN_evaluations()<evaluations_limit_){

            archipelago.resetMigrationStatus();

            island_id = 0;

            // Initialize list to store all the fitness values for this generation (useful for calculating archipelago statistics)
            archipelagoFitnessValues = new ArrayList<>();

            for (Island island: archipelago.islands) {

                island_id++;

                // System.out.println("------- Generation " + Integer.toString(evals/(numIslands*populationSize)) + " -------");
                Population population = island.population;

                // Select parents
                population.selectParents();
                assert (!population.matingPool.isEmpty());
                // System.out.println("Finished parent selection with " + Integer.toString(population.matingPool.size()) + " candidates.");

                // Apply crossover / mutation operators
                // System.out.println("Making babies with recombination " + recombinationStrategy);
                population.makeBabies();
                // System.out.println("Made " + Integer.toString(population.offsprings.size()) + " babies");

                // System.out.println("Evaluating newborns");
                if (!mutationStrategy.equals("non-uniform-ctrl-adap")) {
                    for (Individual child : population.offsprings) {
                        // Check fitness of unknown fuction
                        child.fitness = (double) evaluation_.evaluate(child.genotype);
                        EvaluationCounter.increaseEvaluation();

                    }
                }

                // Select survivors
                population.selectSurvivors();

                // Calculate and print fitness statistics for this island for this generation
	            if (gather){
	                for (int i = 0; i < population.population.size(); i++){
                        System.out.println((Double.toString(population.population.get(i).fitness)));
                    }
		            printing.printStats(archipelago.age, island_id, false, population.calculateFitnessStatistics());
	            }

            }
//
//            if (archipelago.checkConvergence())
//                archipelago.migrate("circle");
//
//
//            archipelago.integrateAllMigrants();
//            archipelago.updateHistory();


	        if(gather){
	            for (int i = 0; i<archipelago.islands.get(0).population.population.size(); i++){
                    System.out.println(archipelago.islands.get(0).population.population.get(i).fitness);
                }
	        	printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics());
	        }

            archipelago.age++;
            //System.out.println(archipelago.age);
        }

    }
}


//	public void run()
//	{
//		// Run your algorithm here
//        System.out.println("Initializing algorithm...");
//
//
//        // ------- PARAMETERS ------- //
//        //      ** GENERAL **         //
//        int populationSize = 10;
//        int offspringsSize = 10;
//        int matingPoolSize = offspringsSize;
//
//        //    ** RECOMBINATION **     //
//        List<String> recombinationStrategies = Arrays.asList(
//                "simple-arith",  // no parameter needed
//                "single-arith",  // no parameter needed
//                "whole-arith",   // no parameter needed
//                "BLX"            // needs parameter alfa
//        );
//        double alfa = 0.5;
//
//        String recombinationStrategy = recombinationStrategies.get(0);
//
//
//        //    ** MUTATION **     //
//        List<String> mutationStrategies = Arrays.asList(
//                "uniform",      // needs parameter mutationRate
//                "non-uniform"   // needs parameter stdDeviation and Mean
//        );
//        double mutationRate = 0.05; // default
//        double stdDeviation = 0.2; // ???
//        double mean = 0;            // ???
//        String mutationStrategy = mutationStrategies.get(1);
//
//        //    ** REPRODUCTION PROBABILITY **     //
//        List<String> reproductionProbabilityStrategies = Arrays.asList(
//                "linear",       // needs parameter s
//                "exponential"   // no parameter needed
//        );
//        double s = 1.5; // should be between 1 and 2
//
//
//        int evals = 0;
//
//        // init population
//        System.out.println("Initializing population - μ: " + Integer.toString(populationSize) + "  λ:" + Integer.toString(offspringsSize));
//
//        Population population = new Population(populationSize, matingPoolSize, offspringsSize);
//        population.setReproductionProbabilityStrategy("linear", s);
//        population.setParentSelectionStrategy("SUS");
//        population.setRecombinationStrategy("simple-arith");
//        population.setMutationStrategy("uniform", 0.05);
//        population.setSurvivorSelectionStrategy("replaceWorst");
//
//        // calculate fitness
//        System.out.println("Calculating fitness of initial population");
//        for(Individual individual: population.population) {
//            individual.fitness = (double) evaluation_.evaluate(individual.genotype);
//            evals++;
//        }
//
//        while(evals<evaluations_limit_){
//            System.out.println("------- Generation " + Integer.toString(evals) + " -------");
//
//            // Select parents
//            population.selectParents();
//            assert (!population.matingPool.isEmpty());
//            System.out.println("Finished parent selection with " + Integer.toString(population.matingPool.size()) + " candidates.");
//
//            // Apply crossover / mutation operators
//            System.out.println("Making babies with recombination " + recombinationStrategy);
//            population.makeBabies();
//            System.out.println("Made " + Integer.toString(population.offsprings.size()) + " babies");
//
//
//            System.out.println("Evaluating newborns");
//            for(Individual child: population.offsprings) {
//                // Check fitness of unknown fuction
//                child.fitness = (double) evaluation_.evaluate(child.genotype);
//                evals++;
//            }
//
//
//
//            // Select survivors
//            population.selectSurvivors();
//        }
//
//	}
