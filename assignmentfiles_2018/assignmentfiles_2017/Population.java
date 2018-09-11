import java.util.ArrayList;

import org.vu.contest.ContestSubmission;

public class Population{
    Integer size;
    
    Integer age;

    ArrayList<Individual> population;
    
    ArrayList<Individual> matingPool;

    ArrayList<Individual> offsprings;

    ContestEvaluation ContestEvaluation;

    public Population(Integer size){
        population = new ArrayList<>();

        double[] baseline_fenotype = {
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0
        };

        for(i = 0; i < size; i++){
            population.add(new Individual(baseline_fenotype));
        }
    }

    public ArrayList<Individual> selectParents(Integer num){
        return new ArrayList<>();
    }

    
    public ArrayList<Individual> makeChildren(ArrayList<Individual> selection){
        return new ArrayList<>();
    }

    public void evaluatePopulation(){
        return;
    }
    

    public void sortPopulationByFitness(){
        return;
    }


    public void createMatingPool(){
        return;
    }


    public void updateGeneration(){
        return;
    }
    
}