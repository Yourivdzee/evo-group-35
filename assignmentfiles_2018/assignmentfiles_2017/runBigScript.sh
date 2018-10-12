#!/bin/sh
for mutationRate in "0.1" "0.2" "0.3" "0.4"; do
    for recombStrat in whole-arith BLX; do
        for populationSize in 1000 3000 7000; do
            for generationGap in 3 4 5 6; do
                for tournamentGap in 20 10 5 2; do

                    offspringSize=$((generationGap*populationSize))
                    tournamentSize=$((populationSize/tournamentGap))

                    echo "1- Number of runs: $1"
                    echo "2- Function to evaluate: $2 (Options: SphereEvaluation, BentCigarFunction, KatsuuraEvaluation, SchaffersEvaluation)"
                    echo "3- Population size: $populationSize"
                    echo "4- Offspring size: $offspringSize"
                    echo "5- Reproduction probability calculation strategy: $5 (Options: linear -> requires parameter s, exponential )"
                    echo "6- Parent selection strategy: $6 (Options: RW, SUS, tournament -> requires parameter k)"
                    echo "7- Recombination strategy: $recombStrat (Options: simple-arith, single-arith, whole-arith, BLX -> requires parameter alfa)"
                    echo "8- Mutation strategy: $8 (Options: uniform -> requires parameter mutationRate, non-uniform -> requires parameters mean, stdDeviation)$8"
                    echo "9- Survivor selection strategy: $9 (Options: tournament -> requires parameter q, mu,lambda, mu+lambda, replaceWorst, age)"
                    echo "10- Alfa=${10}"
                    echo "11- k=$tournamentSize"
                    echo "12- mutationRate=$mutationRate"
                    echo "13- mean=${13}"
                    echo "14- stdDeviation=${14}"
                    echo "15- s=${15}"
                    run="javac -cp contest.jar Group35.java Individual.java Population.java Island.java Archipelago.java Printing.java EvaluationCounter.java && jar cmf MainClass.txt submission.jar Group35.class Individual.class EvaluationCounter.class Population.class Island.class Archipelago.class Printing.class &&
                         java -DpopulationSize=$populationSize -DoffspringSize=$offspringSize -DreprodStrat=$5 -DparentSelectStrat=$6 -DrecombStrat=$recombStrat -DmutateStrat=$8 -DsurvivorSelectionStrat=$9 -Dalfa=${10} -Dk=$tournamentSize -DmutationRate=$mutationRate -Dmean=${13} -DstdDeviation=${14} -Ds=${15} -Devaluation=$2 -jar testrun.jar -submission=Group35 -evaluation=$2 -seed=1"

                    TIMEDATE=$(date --rfc-3339=seconds)
                    END=$1
                    ##done
                    #for databaseName in a b c d e f; do
                    #    echo $databaseName
                    #done

                    for i in $(seq 1 $END);
                        do mkdir -p "data/$2/bigRun/$TIMEDATE" && echo "Run $i..." && eval $run > "data/$2/bigRun/$TIMEDATE/run$i.txt";
                    done
                done
            done
        done
    done
done