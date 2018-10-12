#!/bin/sh

echo "1- Number of runs: $1"
echo "2- Function to evaluate: $2 (Options: SphereEvaluation, BentCigarFunction, KatsuuraEvaluation, SchaffersEvaluation)"
echo "3- Population size: $3"
echo "4- Offspring size: $4"
echo "5- Reproduction probability calculation strategy: $5 (Options: linear -> requires parameter s, exponential )"
echo "6- Parent selection strategy: $6 (Options: RW, SUS, tournament -> requires parameter k)"
echo "7- Recombination strategy: $7 (Options: simple-arith, single-arith, whole-arith, BLX -> requires parameter alfa)"
echo "8- Mutation strategy: $8 (Options: uniform -> requires parameter mutationRate, non-uniform -> requires parameters mean, stdDeviation)$8"
echo "9- Survivor selection strategy: $9 (Options: tournament -> requires parameter q, mu,lambda, mu+lambda, replaceWorst, age)"
echo "10- Alfa=${10}"
echo "11- k=$11"
echo "12- mutationRate=${12}"
echo "13- mean=${13}"
echo "14- stdDeviation=${14}"
echo "15- s=${15}"
run="javac -cp contest.jar Group35.java Individual.java Population.java Island.java Archipelago.java Printing.java EvaluationCounter.java && jar cmf MainClass.txt submission.jar Group35.class Individual.class EvaluationCounter.class Population.class Island.class Archipelago.class Printing.class &&
     java -DpopulationSize=$3 -DoffspringSize=$4 -DreprodStrat=$5 -DparentSelectStrat=$6 -DrecombStrat=$7 -DmutateStrat=$8 -DsurvivorSelectionStrat=$9 -Dalfa=${10} -Dk=${11} -DmutationRate=${12} -Dmean=${13} -DstdDeviation=${14} -Ds=${15} -Devaluation=$2 -jar testrun.jar -submission=Group35 -evaluation=$2 -seed=1"


TIMEDATE=$(date --rfc-3339=seconds)
END=$1
##done
#for databaseName in a b c d e f; do
#    echo $databaseName
#done

for i in $(seq 1 $END);
    do mkdir -p "data/$2/$TIMEDATE" && echo "Run $i..." && eval $run > "data/$2/$TIMEDATE/run$i.txt";
done
