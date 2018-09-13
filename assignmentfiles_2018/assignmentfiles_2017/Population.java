import java.util.ArrayList;
import java.util.Arrays;

public class Population{
    Integer size;
    
    Integer age;

    ArrayList<Individual> population;
    
    ArrayList<Individual> matingPool;

    ArrayList<Individual> offsprings;

    public Population(Integer size){
        population = new ArrayList<>();

        for(int i = 0; i < size; i++){
            Individual indiv = new Individual();
            population.add(indiv);
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