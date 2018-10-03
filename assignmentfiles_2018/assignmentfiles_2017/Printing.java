import java.util.ArrayList;

public class Printing{

	public Printing(){
		
	}

	public void printIslandStats(int epoch, int islandId, double max, double mean, double stdv){
        System.out.println(Integer.toString(epoch) + " " + 
             Integer.toString(islandId) + " " + 
             "false" + " " +
             String.format("%.5f", max) + " " +
             String.format("%.5f", mean) + " " + 
             String.format("%.5f", stdv));

	}

	public void printArchipelagoStats(int epoch, boolean exchange, int archipelagoSize, ArrayList<Double> fitnessValues){

        Double max = 0.0;
        Double sum = 0.0;
        for (double fitnessValue: fitnessValues) {
            sum += fitnessValue;
            if (fitnessValue > max){
                max = fitnessValue;
            }
        }
        Double mean = sum / archipelagoSize;

        Double stdv = 0.0;
        for (double fitnessValue: fitnessValues) {
            stdv = Math.pow(fitnessValue - mean, 2);
        }
        stdv = Math.sqrt(stdv / archipelagoSize);

        System.out.println(Integer.toString(epoch) + " " + 
             "0" + " " + 
             String.valueOf(exchange) + " " +
             String.format("%.5f", max) + " " +
             String.format("%.5f", mean) + " " + 
             String.format("%.5f", stdv));

	}

}

