#!/usr/bin/env bash
export LD_LIBRARY_PATH=$PWD

#!/bin/sh

#
#for numIslands in 2 4 5; do
#    for populationSize in 6 8 10 12; do
echo "1- Number of runs: $1"
echo "2- Function to evaluate: $2 (Options: SphereEvaluation, BentCigarFunction, KatsuuraEvaluation, SchaffersEvaluation)"
echo "3- Population size: $3"
echo "4- Offspring size: $4"
echo "5- Reproduction probability calculation strategy: $5 (Options: linear -> requires parameter s, exponential )"
echo "6- Parent selection strategy: $6 (Options: RW, SUS, tournament -> requires parameter k)"
echo "7- Recombination strategy: $7 (Options: simple-arith, single-arith, whole-arith, BLX -> requires parameter alfa)"
echo "8- Mutation strategy: $8 (Options: uniform -> requires parameter mutationRate, non-uniform -> requires parameters mean, stdDeviation)$8"
echo "9- Survivor selection strategy: $9 (Options: tournament -> requires parameter q, mu,lambda, mu+lambda, replaceWorst, age)"
echo "Island Configuration:"
echo "10 - Number of Islands: ${10}"
echo "11 - Number of Epochs: ${11}"
echo "12 - Epoch Strategy: ${12}"
echo "13 - nExchangeInd: ${13}"
echo "14 - Migration strategy: ${14}"
echo "Parameter Configuration:"
echo "14- Alfa=${15}"
echo "15- k=${16}"
echo "16- mutationRate=${17}"
echo "17- mean=${18}"
echo "18- stdDeviation=${19}"
echo "19- s=${20}"
run="javac -cp contest.jar Group35.java Individual.java EvaluationCounter.java Population.java Island.java Archipelago.java Printing.java && jar cmf MainClass.txt submission.jar Group35.class EvaluationCounter.class Individual.class Population.class Island.class Archipelago.class Printing.class &&
     java -DpopulationSize=$3 -DoffspringSize=$4 -DreprodStrat=$5 -DparentSelectStrat=$6 -DrecombStrat=$7 -DmutateStrat=$8 -DsurvivorSelectionStrat=$9 -DnIslands=${10} -DnEpochs=${11} -DnEpochStrat=${12} -DnExchangeInd=${13} -DnMigrationStrat=${14} -Dalfa=${15} -Dk=${16} -DmutationRate=${17} -Dmean=${18} -DstdDeviation=${19} -Ds=${20} -jar testrun.jar -submission=Group35 -evaluation=$2 -seed=1"


TIMEDATE=$(date --rfc-3339=seconds)
END=$1
##done
#for databaseName in a b c d e f; do
#    echo $databaseName
#done

for i in $(seq 1 $END);
    do mkdir -p "data/$2/$TIMEDATE" && echo "Run $i..." && eval $run > "data/$2/$TIMEDATE/run$i.txt";
done
#    done
#done