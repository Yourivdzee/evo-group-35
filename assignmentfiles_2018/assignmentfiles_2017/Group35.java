import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Group35 implements ContestSubmission {
    Random rnd_;
    String function_name;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;

    public Group35() {
        rnd_ = new Random();
    }

    public void setSeed(long seed) {
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
    }

    public void setEvaluation(ContestEvaluation evaluation) {
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

//    public void run()
//    {
//        // Run your algorithm here
//        // System.out.println("Initializing algorithm...");
//
//        // gather stats for population and archipelago (stats collection may slow down the process)
//        boolean gather = true;
//
//        int numIslands = 20;
//
//        int populationSize = evaluations_limit_ / (numIslands * 100);
//
//        int numExchangeIndividuals = 2;
//        int epoch = 10;
//
//        int archipelagoSize = numIslands * populationSize;
//        int matingPoolSize = populationSize;
//        int offspringsSize = populationSize;
//
//        // Initialize printer class
//        Printing printing = new Printing();
//
//        // ------- PARAMETERS ------- //
//        //      * GENERAL *         //
//
//        //    * RECOMBINATION *     //
//        List<String> recombinationStrategies = Arrays.asList(
//                "simple-arith",  // no parameter needed
//                "single-arith",  // no parameter needed
//                "whole-arith",   // needs parameter alfa
//                "BLX"            // needs parameter alfa
//        );
//        double alfa = 0.5;
//
//        String recombinationStrategy = recombinationStrategies.get(0);
//
//
//        //    * MUTATION *     //
//        List<String> mutationStrategies = Arrays.asList(
//                "uniform",      // needs parameter mutationRate
//                "non-uniform"   // needs parameter stdDeviation and Mean
//        );
//        double mutationRate = 0.05; // default
//        double stdDeviation = 0.1; // ???
//        double mean = 0;            // ???
//        String mutationStrategy = mutationStrategies.get(1);
//
//        //    * REPRODUCTION PROBABILITY *     //
//        List<String> reproductionProbabilityStrategies = Arrays.asList(
//                "linear",       // needs parameter s
//                "exponential"   // no parameter needed
//        );
//        double s = 1.5; // should be between 1 and 2
//
//
//        int evals = 0;
//
//        // Column names for output file
//        if (gather){
//            System.out.println("Generation IslandId Exchange Maximum Average StandardDev");
//        }
//
//
//        Archipelago archipelago = new Archipelago(numIslands,numExchangeIndividuals, epoch);
//
//        // init population
//        // System.out.println("Initializing population - μ: " + Integer.toString(populationSize) + "  λ:" + Integer.toString(offspringsSize));
//
//        for(int i = 0; i < archipelago.size; i++){
//            Population population = new Population(populationSize, matingPoolSize, offspringsSize);
//            population.setReproductionProbabilityStrategy("linear", s);
//            population.setParentSelectionStrategy("tournament", populationSize / 10);
//            population.setRecombinationStrategy("whole-arith", alfa);
//            population.setMutationStrategy("non-uniform", stdDeviation, mean);
//            // population.setMutationStrategy("uniform", mutationRate);
//            population.setSurvivorSelectionStrategy("replaceWorst");
//            archipelago.islands.get(i).populate(population);
//        }
//
//        // calculate fitness
//        // System.out.println("Calculating fitness of initial population");
//
//        ArrayList<Double> archipelagoFitnessValues = new ArrayList<>();
//        int island_id = 0;
//
//        for(Island island: archipelago.islands) {
//            island_id++;
//            Population population = island.population;
//            for (Individual individual : population.population) {
//                individual.fitness = (double) evaluation_.evaluate(individual.genotype);
//                evals++;
//
//            }
//
//            // calculate and print initial fintess statistics for this island
//            if (gather){
//                printing.printStats(archipelago.age, island_id, false, population.calculateFitnessStatistics());
//            }
//
//            // calculate and print initial statistics for the archipelago
//            if(gather){
//                printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics());
//            }
//
//        }
//
//        while(evals<evaluations_limit_){
//
//            archipelago.resetMigrationStatus();
//
//            island_id = 0;
//
//            // Initialize list to store all the fitness values for this generation (useful for calculating archipelago statistics)
//            archipelagoFitnessValues = new ArrayList<>();
//
//            for (Island island: archipelago.islands) {
//
//                island_id++;
//
//                // System.out.println("------- Generation " + Integer.toString(evals/(numIslands*populationSize)) + " -------");
//                Population population = island.population;
//
//                // Select parents
//                population.selectParents();
//                assert (!population.matingPool.isEmpty());
//                // System.out.println("Finished parent selection with " + Integer.toString(population.matingPool.size()) + " candidates.");
//
//                // Apply crossover / mutation operators
//                // System.out.println("Making babies with recombination " + recombinationStrategy);
//                population.makeBabies();
//                // System.out.println("Made " + Integer.toString(population.offsprings.size()) + " babies");
//
//                // System.out.println("Evaluating newborns");
//                for (Individual child : population.offsprings) {
//                    // Check fitness of unknown fuction
//                    child.fitness = (double) evaluation_.evaluate(child.genotype);
//                    evals++;
//
//                }
//
//                // Select survivors
//                population.selectSurvivors();
//
//                // Calculate and print fitness statistics for this island for this generation
//                if (gather){
//                    printing.printStats(archipelago.age, island_id, false, population.calculateFitnessStatistics());
//                }
//
//            }
//
//            // if (archipelago.checkConvergence())
//            if (archipelago.age % epoch == 0 && archipelago.age > 0)
//                archipelago.migrate("star");
//
//
//            archipelago.integrateAllMigrants();
//            archipelago.updateHistory();
//
//
//            if(gather){
//                printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics());
//            }
//
//            archipelago.age++;
//        }
//
//    }


//    public void run()
//    {
//        // Run your algorithm here
//        // System.out.println("Initializing algorithm...");
//
//        // gather stats for population and archipelago (stats collection may slow down the process)
//        boolean gather = true;
//
//    	// Initialize printer class
//    	Printing printing = new Printing();
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
//        // Column names for output file
//        if (gather){
//	        System.out.println("Generation IslandId Exchange Maximum Average StandardDev");
//		}
//
//        int numIslands = 5;
//        int numExchangeIndividuals = 2;
//        int epoch = 10;
//        int archipelagoSize = numIslands * populationSize;
//
//        Archipelago archipelago = new Archipelago(numIslands,numExchangeIndividuals, epoch);
//
//        // init population
//        // System.out.println("Initializing population - μ: " + Integer.toString(populationSize) + "  λ:" + Integer.toString(offspringsSize));
//
//        for(int i = 0; i < archipelago.size; i++){
//            Population population = new Population(populationSize, matingPoolSize, offspringsSize);
//            population.setReproductionProbabilityStrategy("linear", s);
//            population.setParentSelectionStrategy("SUS");
//            population.setRecombinationStrategy("simple-arith");
//            population.setMutationStrategy("uniform", 0.05);
//            population.setSurvivorSelectionStrategy("replaceWorst");
//            archipelago.islands.get(i).populate(population);
//        }
//
//        // calculate fitness
//        // System.out.println("Calculating fitness of initial population");
//
//        ArrayList<Double> archipelagoFitnessValues = new ArrayList<>();
//        int island_id = 0;
//
//        for(Island island: archipelago.islands) {
//            island_id++;
//            Population population = island.population;
//            for (Individual individual : population.population) {
//                individual.fitness = (double) evaluation_.evaluate(individual.genotype);
//                evals++;
//
//            }
//
//            // calculate and print initial fintess statistics for this island
//            if (gather){
//	            printing.printStats(archipelago.age, island_id, false, population.calculateFitnessStatistics());
//            }
//
//        // calculate and print initial statistics for the archipelago
//        if(gather){
//        	printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics());
//        }
//
//        }
//
//        while(evals<evaluations_limit_){
//
//            archipelago.resetMigrationStatus();
//
//            island_id = 0;
//
//            // Initialize list to store all the fitness values for this generation (useful for calculating archipelago statistics)
//            archipelagoFitnessValues = new ArrayList<>();
//
//            for (Island island: archipelago.islands) {
//
//                island_id++;
//
//                // System.out.println("------- Generation " + Integer.toString(evals/(numIslands*populationSize)) + " -------");
//                Population population = island.population;
//
//                // Select parents
//                population.selectParents();
//                assert (!population.matingPool.isEmpty());
//                // System.out.println("Finished parent selection with " + Integer.toString(population.matingPool.size()) + " candidates.");
//
//                // Apply crossover / mutation operators
//                // System.out.println("Making babies with recombination " + recombinationStrategy);
//                population.makeBabies();
//                // System.out.println("Made " + Integer.toString(population.offsprings.size()) + " babies");
//
//                // System.out.println("Evaluating newborns");
//                for (Individual child : population.offsprings) {
//                    // Check fitness of unknown fuction
//                    child.fitness = (double) evaluation_.evaluate(child.genotype);
//                    evals++;
//
//                }
//
//                // Select survivors
//                population.selectSurvivors();
//
//                // Calculate and print fitness statistics for this island for this generation
//	            if (gather){
//		            printing.printStats(archipelago.age, island_id, false, population.calculateFitnessStatistics());
//	            }
//
//            }
//
//            if (archipelago.checkConvergence())
//                archipelago.migrate("circle");
//
//
//            archipelago.integrateAllMigrants();
//            archipelago.updateHistory();
//
//
//	        if(gather){
//	        	printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics());
//	        }
//
//            archipelago.age++;
//        }
//
//    }
//}


    public void run() {
        // Run your algorithm here
        System.out.println("Initializing algorithm...");

        Archipelago archipelago = new Archipelago(1,1,1);
        assert archipelago.size == 1;

        Printing printing = new Printing();
        // ------- PARAMETERS ------- //

        // POPULATION SIZE
        int populationSize = Integer.parseInt(System.getProperty("populationSize"));

        // OFFSPRINGS SIZE
        int offspringsSize = Integer.parseInt(System.getProperty("offspringSize"));

        // MATING POOL SIZE
        int matingPoolSize = offspringsSize;

        Population pop = new Population(populationSize, matingPoolSize, offspringsSize);

        // REPRODUCTION PROBABILITY STRATEGY
        String reprodStrat = System.getProperty("reprodStrat");
        double s = 0;
        if (reprodStrat.equals("linear")) {
            s = Double.parseDouble(System.getProperty("s"));
            pop.setReproductionProbabilityStrategy(reprodStrat, s);
            System.out.println("Reproduction probability strategy: " + reprodStrat + " with s=" + Double.toString(s));
        } else {
            System.out.println("Reproduction probability strategy: " + reprodStrat);
            pop.setReproductionProbabilityStrategy(reprodStrat);
        }

        // PARENT SELECTION STRATEGY
        String parentSelectStrat = System.getProperty("parentSelectStrat");
        int k;
        if (parentSelectStrat.equals("tournament")) {
            k = Integer.parseInt(System.getProperty("k"));
            pop.setParentSelectionStrategy(parentSelectStrat, k);
            System.out.println("Parent selection strategy: " + parentSelectStrat + " with k=" + Double.toString(k));
        } else {
            pop.setParentSelectionStrategy(parentSelectStrat);
            System.out.println("Parent selection strategy: " + parentSelectStrat);
        }

        // RECOMBINATION STRATEGY
        String recombStrat = System.getProperty("recombStrat");
        double alfa;
        if (recombStrat.equals("BLX")) {
            alfa = Double.parseDouble(System.getProperty("alfa"));
            pop.setRecombinationStrategy(recombStrat, alfa);
            System.out.println("Recombination strategy: " + recombStrat + " with alfa=" + Double.toString(alfa));
        } else {
             pop.setRecombinationStrategy(recombStrat);
             System.out.println("Recombination strategy: " + recombStrat);
        }

        // MUTATION STRATEGY
        String mutateStrat = System.getProperty("mutateStrat");
        double mutationRate = 0;
        double stdDeviaton = 0;
        double mean = 0;
        if (mutateStrat.equals("uniform")) {
            mutationRate = Double.parseDouble(System.getProperty("mutationRate"));
            pop.setMutationStrategy(mutateStrat, mutationRate);
            System.out.println("Mutation strategy: " + mutateStrat+ " with mutationRate=" + Double.toString(mutationRate));
        } else if (mutateStrat.equals("non-uniform")) {
            mean = Double.parseDouble(System.getProperty("mean"));
            stdDeviaton = Double.parseDouble(System.getProperty("stdDeviation"));
            pop.setMutationStrategy(mutateStrat, stdDeviaton, mean);
            System.out.println("Mutation strategy: " + mutateStrat + " with stdDeviation= " + Double.toString(stdDeviaton) + " and mean: " + Double.toString(mean));
        }

        // SURVIVOR SELECTION STRATEGY
        String survivorSelectionStrat = System.getProperty("survivorSelectionStrat");
        int q = 0;
        if (survivorSelectionStrat.equals("tournament")) {
            q = Integer.parseInt(System.getProperty("q"));
            pop.setSurvivorSelectionStrategy(survivorSelectionStrat, q);
            System.out.println("Survivor selection strategy: " + survivorSelectionStrat+ " with q=" + Double.toString(q));
        } else {
            pop.setSurvivorSelectionStrategy(survivorSelectionStrat, q);
            System.out.println("Survivor selection strategy: " + survivorSelectionStrat);
        }

        for(int i = 0; i < archipelago.size; i++) {
            archipelago.islands.get(i).populate(pop);
        }

        System.out.println("Generation IslandId Exchange Maximum Average StandardDev");

        int evals = 0;
        for(Island island: archipelago.islands) {
            Population islandPopulation = island.population;
            for (Individual individual : islandPopulation.population) {
                individual.fitness = (double) evaluation_.evaluate(individual.genotype);
                evals++;

            }

            // calculate and print initial fintess statistics for this island
            //printing.printStats(archipelago.age, 1, false, islandPopulation.calculateFitnessStatistics());


            // calculate and print initial statistics for the archipelago
            //printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics());


        }


        while(evals<evaluations_limit_){

            archipelago.resetMigrationStatus();

            int island_id = 0;

            // Initialize list to store all the fitness values for this generation (useful for calculating archipelago statistics)
            ArrayList<Double> archipelagoFitnessValues = new ArrayList<>();


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
                for (Individual child : population.offsprings) {
                    // Check fitness of unknown fuction
                    child.fitness = (double) evaluation_.evaluate(child.genotype);
                    evals++;

                }

                // Select survivors
                population.selectSurvivors();

                // Calculate and print fitness statistics for this island for this generation

//                printing.printStats(archipelago.age, island_id, false, population.calculateFitnessStatistics());


            }

            // if (archipelago.checkConvergence())
            //if (archipelago.age % archipelago.epoch == 0 && archipelago.age > 0)
              //  archipelago.migrate("star");


            //archipelago.integrateAllMigrants();
            //archipelago.updateHistory();



            printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics());


            archipelago.age++;
        }

//        while (evals < evaluations_limit_) {
//            System.out.println("------- Generation " + Integer.toString(evals) + " -------");
//
//            // Select parents
//            population.selectParents();
//            assert (population.matingPool.size() == population.matingPoolSize);
//            System.out.println("#Mating pool: " + Integer.toString(population.matingPool.size()));
//
//            // Apply crossover / mutation operators
//            population.makeBabies();
//            System.out.println("#Offsprings" + Integer.toString(population.offsprings.size()));
//
//            for (Individual child : population.offsprings) {
//                // Check fitness of unknown fuction
//                child.fitness = (double) evaluation_.evaluate(child.genotype);
//                evals++;
//            }
//
//
//            // Select survivors
//            population.selectSurvivors();
//
//        }

    }
}