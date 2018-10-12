import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.*;

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

    @Override
    public void run() {
        run_script();
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


    public void run_script() {
        String func = System.getProperty("evaluation");
        // Run your algorithm here
        System.out.println("Initializing algorithm on function: " + func);
        int n_islands = 1;
        int n_exch_ind = 1;
        int n_epochs = 1;

        Archipelago archipelago = new Archipelago(n_islands, n_exch_ind, n_epochs);

        Printing printing = new Printing();
        // ------- PARAMETERS ------- //

        // POPULATION SIZE
        int populationSize = Integer.parseInt(System.getProperty("populationSize"));
        System.out.println("Population size: "+ Integer.toString(populationSize));

        // OFFSPRINGS SIZE
        int offspringsSize = Integer.parseInt(System.getProperty("offspringSize"));
        System.out.println("Offspring size: "+ Integer.toString(offspringsSize));

        // MATING POOL SIZE
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
        double c = 0;
        double c_prime = 0;
        double e = 0;
        double b = 0;

        if (mutateStrat.equals("uniform")) {
            mutationRate = Double.parseDouble(System.getProperty("mutationRate"));
            pop.setMutationStrategy(mutateStrat, mutationRate);
            System.out.println("Mutation strategy: " + mutateStrat + " with mutationRate=" + Double.toString(mutationRate));
        } else if (mutateStrat.equals("non-uniform")){
            mean = Double.parseDouble(System.getProperty("mean"));
            stdDeviaton = Double.parseDouble(System.getProperty("stdDeviation"));
            pop.setMutationStrategy(mutateStrat, stdDeviaton, mean);
            System.out.println("Mutation strategy: " + mutateStrat + " with stdDeviation= " + Double.toString(stdDeviaton) + " and mean: " + Double.toString(mean));
        } else if (mutateStrat.equals("uncorrelated-with-n-steps")){
            c = Double.parseDouble(System.getProperty("c"));
            c_prime = Double.parseDouble(System.getProperty("c_prime"));
            e = Double.parseDouble(System.getProperty("e"));
            pop.setMutationStrategy(mutateStrat, c, c_prime, e);
            System.out.println("Mutation strategy: " + mutateStrat + " with c= " + Double.toString(c) + " c_prime= " + Double.toString(c_prime) + " and e= " + Double.toString(e));
        } else if (mutateStrat.equals("correlated")){
            c = Double.parseDouble(System.getProperty("c"));
            c_prime = Double.parseDouble(System.getProperty("c_prime"));
            e = Double.parseDouble(System.getProperty("e"));
            b = Double.parseDouble(System.getProperty("b"));
            pop.setMutationStrategy(mutateStrat, c, c_prime, e, b);
            System.out.println("Mutation strategy: " + mutateStrat + " with c= " + Double.toString(c) + " c_prime= " + Double.toString(c_prime) + " e= " + Double.toString(e) + " and b= " + Double.toString(b)); 
        }

        // SURVIVOR SELECTION STRATEGY
        String survivorSelectionStrat = System.getProperty("survivorSelectionStrat");
        int q = 0;
        if (survivorSelectionStrat.equals("tournament")) {
            q = Integer.parseInt(System.getProperty("q"));
            pop.setSurvivorSelectionStrategy(survivorSelectionStrat, q);
            System.out.println("Survivor selection strategy: " + survivorSelectionStrat + " with q=" + Double.toString(q));
        } else {
            pop.setSurvivorSelectionStrategy(survivorSelectionStrat, q);
            System.out.println("Survivor selection strategy: " + survivorSelectionStrat);
        }

        for (int i = 0; i < archipelago.size; i++) {
            archipelago.islands.get(i).populate(pop);
        }

        // Column names for output file
        System.out.println("Generation IslandId Exchange Maximum Average StandardDev Evals");

        for (Island island : archipelago.islands) {
            Population islandPopulation = island.population;
            for (Individual child : islandPopulation.offsprings) {
                // Check fitness of unknown fuction
                child.fitness = (double) evaluation_.evaluate(child.genotype);
                EvaluationCounter.increaseEvaluation();
            }
        }


        while (EvaluationCounter.getN_evaluations() < evaluations_limit_) {

            archipelago.resetMigrationStatus();

            int island_id = 0;

            // Initialize list to store all the fitness values for this generation (useful for calculating archipelago statistics)
            ArrayList<Double> archipelagoFitnessValues = new ArrayList<>();


            for (Island island : archipelago.islands) {

                island_id++;

                Population population = island.population;

                // Select parents
                population.selectParents();
                assert (!population.matingPool.isEmpty());

                // Apply crossover / mutation operators
                population.makeBabies();

                if (!mutateStrat.equals("non-uniform-ctrl-adap") ) {
                    for (Individual child : population.offsprings) {
                        // Check fitness of unknown fuction
                        child.fitness = (double) evaluation_.evaluate(child.genotype);
                        EvaluationCounter.increaseEvaluation();
                    }
                }

                // Select survivors
                population.selectSurvivors();

                printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics(),EvaluationCounter.getN_evaluations());

            }
            archipelago.age++;
        }
    }

    public void run_archipeligo() {
        // Run your algorithm here
        System.out.println("Initializing algorithm...");
        int n_islands = Integer.parseInt(System.getProperty("nIslands"));
        int n_exch_ind = Integer.parseInt(System.getProperty("nExchangeInd"));
        int n_epochs = Integer.parseInt(System.getProperty("nEpochs"));


        Archipelago archipelago = new Archipelago(n_islands, n_exch_ind, n_epochs);

        Printing printing = new Printing();
        // ------- PARAMETERS ------- //

        // POPULATION SIZE
        int populationSize = Integer.parseInt(System.getProperty("populationSize"));
        System.out.println("Population size: "+ Integer.toString(populationSize));
        // OFFSPRINGS SIZE
        int offspringsSize = Integer.parseInt(System.getProperty("offspringSize"));
        System.out.println("Offspring size: "+ Integer.toString(offspringsSize));

        // MATING POOL SIZE
        int matingPoolSize = offspringsSize;

        Population pop = new Population(evaluation_, populationSize, matingPoolSize, offspringsSize);
        //    ** MUTATION **     //
        List<String> mutationStrategies = Arrays.asList(
                "uniform",      // needs parameter mutationRate
                "non-uniform",   // needs parameter stdDeviation and Mean
                "non-uniform-ctrl-det",
                "non-uniform-ctrl-adap"
        );

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
        double c = 0;
        double c_prime = 0;
        double e = 0;
        double b = 0;

        if (mutateStrat.equals("uniform")) {
            mutationRate = Double.parseDouble(System.getProperty("mutationRate"));
            pop.setMutationStrategy(mutateStrat, mutationRate);
            System.out.println("Mutation strategy: " + mutateStrat + " with mutationRate=" + Double.toString(mutationRate));
        } else if (mutateStrat.equals("non-uniform")){
            mean = Double.parseDouble(System.getProperty("mean"));
            stdDeviaton = Double.parseDouble(System.getProperty("stdDeviation"));
            pop.setMutationStrategy(mutateStrat, stdDeviaton, mean);
            System.out.println("Mutation strategy: " + mutateStrat + " with stdDeviation= " + Double.toString(stdDeviaton) + " and mean: " + Double.toString(mean));
        } else if (mutateStrat.equals("uncorrelated-with-n-steps")){
            c = Double.parseDouble(System.getProperty("c"));
            c_prime = Double.parseDouble(System.getProperty("c_prime"));
            e = Double.parseDouble(System.getProperty("e"));
            pop.setMutationStrategy(mutateStrat, c, c_prime, e);
            System.out.println("Mutation strategy: " + mutateStrat + " with c= " + Double.toString(c) + " c_prime= " + Double.toString(c_prime) + " and e= " + Double.toString(e));
        } else if (mutateStrat.equals("correlated")){
            c = Double.parseDouble(System.getProperty("c"));
            c_prime = Double.parseDouble(System.getProperty("c_prime"));
            e = Double.parseDouble(System.getProperty("e"));
            b = Double.parseDouble(System.getProperty("b"));
            pop.setMutationStrategy(mutateStrat, c, c_prime, e, b);
            System.out.println("Mutation strategy: " + mutateStrat + " with c= " + Double.toString(c) + " c_prime= " + Double.toString(c_prime) + " e= " + Double.toString(e) + " and b= " + Double.toString(b)); 
        }

        // SURVIVOR SELECTION STRATEGY
        String survivorSelectionStrat = System.getProperty("survivorSelectionStrat");
        int q = 0;
        if (survivorSelectionStrat.equals("tournament")) {
            q = Integer.parseInt(System.getProperty("q"));
            pop.setSurvivorSelectionStrategy(survivorSelectionStrat, q);
            System.out.println("Survivor selection strategy: " + survivorSelectionStrat + " with q=" + Double.toString(q));
        } else {
            pop.setSurvivorSelectionStrategy(survivorSelectionStrat, q);
            System.out.println("Survivor selection strategy: " + survivorSelectionStrat);
        }

        for (int i = 0; i < archipelago.size; i++) {
            archipelago.islands.get(i).populate(pop);
        }

        // Column names for output file
        System.out.println("Generation IslandId Exchange Maximum Average StandardDev Evals");

        for (Island island : archipelago.islands) {
            Population islandPopulation = island.population;
            for (Individual child : islandPopulation.offsprings) {
                // Check fitness of unknown fuction
                child.fitness = (double) evaluation_.evaluate(child.genotype);
                EvaluationCounter.increaseEvaluation();
            }
        }


        while (EvaluationCounter.getN_evaluations() < evaluations_limit_) {

            archipelago.resetMigrationStatus();

            int island_id = 0;

            // Initialize list to store all the fitness values for this generation (useful for calculating archipelago statistics)
            ArrayList<Double> archipelagoFitnessValues = new ArrayList<>();


            for (Island island : archipelago.islands) {

                island_id++;

                Population population = island.population;

                // Select parents
                population.selectParents();
                assert (!population.matingPool.isEmpty());

                // Apply crossover / mutation operators
                population.makeBabies();

                if (!mutateStrat.equals("non-uniform-ctrl-adap") ) {
                    for (Individual child : population.offsprings) {
                        // Check fitness of unknown fuction
                        child.fitness = (double) evaluation_.evaluate(child.genotype);
                        EvaluationCounter.increaseEvaluation();
                    }
                }

                // Select survivors
                population.selectSurvivors();

                printing.printStats(archipelago.age, 0, archipelago.checkMigrationStatus(), archipelago.calculateFitnessStatistics(),EvaluationCounter.getN_evaluations());

                archipelago.age++;

            }
        }
    }
}