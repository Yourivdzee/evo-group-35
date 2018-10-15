import java.util.ArrayList;

public class Printing{

	public Printing(){
		
	}

	public void printStats(int epoch, int islandId, boolean exchange, ArrayList<Double> stats, int evals){
        System.out.println(Integer.toString(epoch) + " " + 
							 Integer.toString(islandId) + " " +
							 String.valueOf(exchange) + " " +
							 String.format("%.5f", stats.get(1)) + " " +
							 String.format("%.5f", stats.get(2)) + " " +
							 String.format("%.5f", stats.get(3)) + " " +
							 Integer.toString(evals));

	}

	public void printStats(int epoch, int islandId, boolean exchange, ArrayList<Double> stats, int evals, long time){
		System.out.println(Integer.toString(epoch) + " " +
							Integer.toString(islandId) + " " +
							String.valueOf(exchange) + " " +
							String.format("%.5f", stats.get(1)) + " " +
							String.format("%.5f", stats.get(2)) + " " +
							String.format("%.5f", stats.get(3)) + " " +
							Integer.toString(evals)             + " " +
							Long.toString(time));

	}

	// public void printArchipelagoStats(int epoch, boolean exchange, int archipelagoSize, ArrayList<Double> stats){

 //        Double max = 0.0;
 //        Double sum = 0.0;
 //        for (double fitnessValue: fitnessValues) {
 //            sum += fitnessValue;
 //            if (fitnessValue > max){
 //                max = fitnessValue;
 //            }
 //        }
 //        Double mean = sum / archipelagoSize;

 //        Double stdv = 0.0;
 //        for (double fitnessValue: fitnessValues) {
 //            stdv += Math.pow(fitnessValue - mean, 2);
 //        }
 //        stdv = Math.sqrt(stdv / archipelagoSize);

 //        System.out.println(Integer.toString(epoch) + " " + 
 //             "0" + " " + 
 //             String.valueOf(exchange) + " " +
 //             String.format("%.5f", max) + " " +
 //             String.format("%.5f", mean) + " " + 
 //             String.format("%.5f", stdv));

	// }

}

