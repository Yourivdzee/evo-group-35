import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.SourceTree;

public class Individual{

    double[] fenotype;

    double[] genotype;

    double fitness;

    public Individual(double[] fenotype){
        // Basic represention of solution
        this.fenotype = fenotype;

        // Advanced representation of solution
        this.genotype = new double[]{
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

    public double[] determineGenotype(){
        double[] genotype = {
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

        return genotype;
    }

    
}