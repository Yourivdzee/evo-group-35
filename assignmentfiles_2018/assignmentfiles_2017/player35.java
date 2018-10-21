import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.*;

public class player35 implements ContestSubmission {
    Random rnd_;
    String function_name;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;
    Population pop;


    public player35() {
        rnd_ = new Random();
    }

    public void setSeed(long seed) {
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
    }

    @Override
    public void run() {

        //int n_islands = Integer.parseInt(System.getProperty("nIslands"));

//        if(n_islands>1)
//            run_archipeligo();
//        else
            run_script();
    }

    public Population setUpPopulation() {
        String func = System.getProperty("evaluation");
        // ------- PARAMETERS ------- //
        int populationSize = Integer.parseInt(System.getProperty("populationSize"));
        int offspringsSize = Integer.parseInt(System.getProperty("offspringSize"));
        String reprodStrat = System.getProperty("reprodStrat");
        String parentSelectStrat = System.getProperty("parentSelectStrat");
        String recombStrat = System.getProperty("recombStrat");
        String mutateStrat = System.getProperty("mutateStrat");
        String survivorSelectionStrat = System.getProperty("survivorSelectionStrat");
        int matingPoolSize = offspringsSize;

        Population pop = new Population(evaluation_, populationSize, matingPoolSize, offspringsSize);

        //    ** MUTATION **     //
        List<String> mutationStrategies = Arrays.asList(
                "uniform",      // needs parameter mutationRate
                "non-uniform",   // needs parameter stdDeviation and Mean
                "non-uniform-ctrl-det",
                "non-uniform-ctrl-adap",
                "correlated-with-n-steps"
        );

        //System.out.println("Initializing algorithm on function: " + func);
        //System.out.println("Population size: "+ Integer.toString(populationSize));
        //System.out.println("Offspring size: "+ Integer.toString(offspringsSize));

        // REPRODUCTION PROBABILITY STRATEGY
        double s = 0;
        switch (reprodStrat) {
            case "linear":
                s = Double.parseDouble(System.getProperty("s"));
                pop.setReproductionProbabilityStrategy(reprodStrat, s);
                //System.out.println("Reproduction probability strategy: " + reprodStrat + " with s=" + Double.toString(s));
            default:
                //System.out.println("Reproduction probability strategy: " + reprodStrat);
                pop.setReproductionProbabilityStrategy(reprodStrat);
        }

        // PARENT SELECTION STRATEGY
        int k;
        switch (parentSelectStrat){
            case "tournament":
                k = Integer.parseInt(System.getProperty("k"));
                pop.setParentSelectionStrategy(parentSelectStrat, k);
                //System.out.println("Parent selection strategy: " + parentSelectStrat + " with k=" + Double.toString(k));
            default:
                pop.setParentSelectionStrategy(parentSelectStrat);
               // System.out.println("Parent selection strategy: " + parentSelectStrat);
        }

        // RECOMBINATION STRATEGY
        double alfa;
        switch (recombStrat){
            case "BLX":
                alfa = Double.parseDouble(System.getProperty("alfa"));
                pop.setRecombinationStrategy(recombStrat, alfa);
                //System.out.println("Recombination strategy: " + recombStrat + " with alfa=" + Double.toString(alfa));
            default:
                pop.setRecombinationStrategy(recombStrat);
                //System.out.println("Recombination strategy: " + recombStrat);
        }


        // MUTATION STRATEGY

        double mutationRate = 0;
        double stdDeviaton = 0;
        double mean = 0;
        double c = 0;
        double c_prime = 0;
        double e = 0;
        double b = 0;

        switch (mutateStrat) {
            case "uniform":
                mutationRate = Double.parseDouble(System.getProperty("mutationRate"));
                pop.setMutationStrategy(mutateStrat, mutationRate);
//                System.out.println("Mutation strategy: " + mutateStrat + " with mutationRate=" + Double.toString(mutationRate));
                break;
            case "non-uniform":
                mean = Double.parseDouble(System.getProperty("mean"));
                stdDeviaton = Double.parseDouble(System.getProperty("stdDeviation"));
                pop.setMutationStrategy(mutateStrat, stdDeviaton, mean);
//                System.out.println("Mutation strategy: " + mutateStrat + " with stdDeviation= " + Double.toString(stdDeviaton) + " and mean: " + Double.toString(mean));
                break;
            case "uncorrelated-with-n-steps":
                c = Double.parseDouble(System.getProperty("c"));
                c_prime = Double.parseDouble(System.getProperty("c_prime"));
                e = Double.parseDouble(System.getProperty("e"));
                pop.setMutationStrategy(mutateStrat, c, c_prime, e);
//                System.out.println("Mutation strategy: " + mutateStrat + " with c= " + Double.toString(c) + " c_prime= " + Double.toString(c_prime) + " and e= " + Double.toString(e));
                break;
            case "correlated":
                c = Double.parseDouble(System.getProperty("c"));
                c_prime = Double.parseDouble(System.getProperty("c_prime"));
                e = Double.parseDouble(System.getProperty("e"));
                b = Double.parseDouble(System.getProperty("b"));
                pop.setMutationStrategy(mutateStrat, c, c_prime, e, b);
//                System.out.println("Mutation strategy: " + mutateStrat + " with c= " + Double.toString(c) + " c_prime= " + Double.toString(c_prime) + " e= " + Double.toString(e) + " and b= " + Double.toString(b));
                break;
            default:
                pop.setMutationStrategy(mutateStrat,stdDeviaton, mean);
//                System.out.println("Mutation strategy: " + mutateStrat);
        }

        // SURVIVOR SELECTION STRATEGY
        int q = 0;
        switch (survivorSelectionStrat){
            case "tournament":
                q = Integer.parseInt(System.getProperty("q"));
                pop.setSurvivorSelectionStrategy(survivorSelectionStrat, q);
//                System.out.println("Survivor selection strategy: " + survivorSelectionStrat + " with q=" + Double.toString(q));
            default:
                pop.setSurvivorSelectionStrategy(survivorSelectionStrat, q);
//                System.out.println("Survivor selection strategy: " + survivorSelectionStrat);
        }

        return pop;
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
            int populationSize = 20;
            int offspringsSize = 20;
            int matingPoolSize = 20;
            String parentSelectStrat = "tournament";
            int k = 10;
            String recombStrat = "whole-arith";
            double alfa = 0.5;
            String mutateStrat = "non-uniform-ctrl-adap";
            double stdDeviation = 0.5;
            double mean = 0;
            String survivorSelectStrat = "";
            Population pop = new Population(evaluation_, populationSize, matingPoolSize, offspringsSize);
            pop.setParentSelectionStrategy(parentSelectStrat, k);
            pop.setReproductionProbabilityStrategy("exponential");
            pop.setRecombinationStrategy(recombStrat, alfa);
            pop.setMutationStrategy(mutateStrat, stdDeviation , mean);
            pop.setSurvivorSelectionStrategy("mu,lambda");
            this.pop = pop;
        } else if (isMultimodal && !hasStructure) {
            function_name = "Katsuura";
            int populationSize = 21000;
            int offspringsSize = 7000;
            int matingPoolSize = 21000;
            String reprodStrat = "linear";
            Double s = 1.2;
            String parentSelectStrat = "tournament";
            int k = 500;
            String recombStrat = "whole-arith";
            double alfa = 0.5;
            String mutateStrat = "correlated";
            double c_prime= 0.05;
            double e = 0.00001;
            double b = 5.0;
            double c = 0.5;
            Population pop = new Population(evaluation_, populationSize, matingPoolSize, offspringsSize);
            pop.setReproductionProbabilityStrategy("linear",1.5);
            pop.setParentSelectionStrategy(parentSelectStrat, k);
            pop.setRecombinationStrategy(recombStrat, alfa);
            pop.setReproductionProbabilityStrategy(reprodStrat, s);
            pop.setMutationStrategy(mutateStrat, c, c_prime, e, b);
            this.pop = pop;
            pop.setSurvivorSelectionStrategy("mu,lambda");
        } else if (isMultimodal && hasStructure) {
            function_name = "Schaffers";
            int populationSize = 20;
            int offspringsSize = 20;
            int matingPoolSize = 20;
            String parentSelectStrat = "tournament";
            int k = 5;
            String recombStrat = "whole-arith";
            double alfa = 0.5;
            String mutateStrat = "non-uniform-ctrl-adap";
            double stdDeviation = 0.5;
            double mean = 0;
            Population pop = new Population(evaluation_, populationSize, matingPoolSize, offspringsSize);
            pop.setParentSelectionStrategy(parentSelectStrat, k);
            pop.setReproductionProbabilityStrategy("exponential");
            pop.setRecombinationStrategy(recombStrat, alfa);
            pop.setSurvivorSelectionStrategy("replaceWorst");
            pop.setMutationStrategy(mutateStrat, stdDeviation , mean);
            this.pop = pop;
        } else {
            function_name = "Sphere (Test function)";
        }
//        System.out.println("FUNC:" + function_name);
    }


    public void run_script() {
//        System.out.println("Hello");
//        if (function_name.equals("Katsuura")) {
//            int n_islands = 1;
//            int n_exch_ind = 1;
//            int n_epochs = 1;
//
//            Archipelago archipelago = new Archipelago(n_islands, n_exch_ind, n_epochs, "none", "none");
//
//            Printing printing = new Printing();
//
//
//            for (int i = 0; i < archipelago.size; i++) {
//                archipelago.islands.get(i).populate(pop);
//            }
//
//            // Column names for output file
//            //System.out.println("Generation IslandId Exchange Maximum Average StandardDev Evals");
//
//            for (Island island : archipelago.islands) {
//                Population islandPopulation = island.population;
//                for (Individual child : islandPopulation.population) {
//                    // Check fitness of unknown fuction
//                    child.fitness = (double) evaluation_.evaluate(child.genotype);
//                    EvaluationCounter.increaseEvaluation();
//                }
//            }
//
//        } else {
            // Run your algorithm here
            int n_islands = 1;
            int n_exch_ind = 1;
            int n_epochs = 1;

            Archipelago archipelago = new Archipelago(n_islands, n_exch_ind, n_epochs, "none", "none");

            Printing printing = new Printing();


            for (int i = 0; i < archipelago.size; i++) {
                archipelago.islands.get(i).populate(pop);
            }

            // Column names for output file
            //System.out.println("Generation IslandId Exchange Maximum Average StandardDev Evals");

            for (Island island : archipelago.islands) {
                Population islandPopulation = island.population;
                for (Individual child : islandPopulation.population) {
                    // Check fitness of unknown fuction
                    child.fitness = (double) evaluation_.evaluate(child.genotype);
                    EvaluationCounter.increaseEvaluation();
                }
            }


            while (EvaluationCounter.getN_evaluations() < evaluations_limit_) {
                for (Island island : archipelago.islands) {
                    Population population = island.population;

                    // Select parents
                    population.selectParents();
                    assert (!population.matingPool.isEmpty());

                    // Apply crossover / mutation operators
                    population.makeBabies();

                    if (!pop.mutationStrat.equals("non-uniform-ctrl-adap")) {
                        for (Individual child : population.offsprings) {
                            // Check fitness of unknown fuction
                            child.fitness = (double) evaluation_.evaluate(child.genotype);
                            EvaluationCounter.increaseEvaluation();
                        }
                    }
                    // Select survivors
                    population.selectSurvivors();
//                    printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics(), EvaluationCounter.getN_evaluations());

                }
                archipelago.age++;
            }
        }


    public void run_archipeligo() {
        // Run your algorithm here

        // ISLANDS PARAMATERS
        int n_islands = Integer.parseInt(System.getProperty("nIslands"));
        int n_exch_ind = Integer.parseInt(System.getProperty("nExchangeInd"));
        int n_epochs = Integer.parseInt(System.getProperty("nEpochs"));
        String migrationStrat = System.getProperty("nMigrationStrat");
        String migrationSeason = System.getProperty("nEpochStrat");


        Archipelago archipelago = new Archipelago(n_islands, n_exch_ind, n_epochs, migrationStrat, migrationSeason);

        Printing printing = new Printing();

        System.out.println("Initializing algorithm...");
        System.out.println("Number of islands: " + Integer.toString(n_islands));
        System.out.println("Number of individuals to exchange: " + Integer.toString(n_exch_ind));
        System.out.println("Epoch: " + Integer.toString(n_epochs));
        System.out.println("Migration strategy: " + migrationStrat);
        System.out.println("Migration season: " + migrationSeason);

        for (int i =0; i < n_islands ; i++) {
            Population pop = setUpPopulation();
            assert(pop.offsprings.isEmpty());
            assert(!pop.population.isEmpty());
            assert (n_exch_ind<pop.population.size()-1);
            archipelago.islands.get(i).populate(pop);

        }


        //System.out.println("Generation IslandId Exchange Maximum Average StandardDev Evals IslandTime");

        for (Island island : archipelago.islands) {
            Population islandPopulation = island.population;
            for (Individual child : islandPopulation.population) {
                // Check fitness of unknown fuction
                child.fitness = (double) evaluation_.evaluate(child.genotype);
                EvaluationCounter.increaseEvaluation();
            }
        }


        while (EvaluationCounter.getN_evaluations() < evaluations_limit_) {

            archipelago.resetMigrationStatus();
            int island_id = 1;
            for (Island island : archipelago.islands) {
                long start = System.currentTimeMillis();
                Population population = island.population;

                // Select parents
                population.selectParents();
                assert (population.matingPoolSize == population.matingPool.size());

                // Apply crossover / mutation operators
                population.makeBabies();

                if (!population.mutationStrat.equals("non-uniform-ctrl-adap") ) {
                    for (Individual child : population.offsprings) {
                        // Check fitness of unknown fuction
                        child.fitness = (double) evaluation_.evaluate(child.genotype);
                        EvaluationCounter.increaseEvaluation();
                    }
                }

                // Select survivors
                population.selectSurvivors();

                long end = System.currentTimeMillis();
                //printing.printStats(archipelago.age, island_id, false, island.population.calculateFitnessStatistics(), EvaluationCounter.getN_evaluations(), end-start);
                island_id++;
            }

            archipelago.checkMigrationSeason();
            archipelago.integrateAllMigrants();
            //printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics(), EvaluationCounter.getN_evaluations());
            archipelago.age++;
        }
    }
}