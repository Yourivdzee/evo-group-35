public class EvaluationCounter {

    private static int n_evaluations = 0;


    public static void increaseEvaluation(){
        n_evaluations++;
    }

    public static int getN_evaluations(){
        return n_evaluations;
    }

}
