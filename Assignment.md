# Overleaf Link for Report

https://www.overleaf.com/project/5bc7051b6c164d337430a1c2

# Specifying the parameters before run

Example Command:

java–Dpopulation=100.0 -jartestrun.jar-submission=player1  - evaluation=BentCigarFunction-seed=1 

Example In Java:

Double.parseDouble(System.getProperty(“var1”)); 

Source: tutorial pdf on Canvas


# Run the Algorithm

javac -cp contest.jar Group35.java Individual.java Population.java Island.java Archipelago.java Printing.java && jar cmf MainClass.txt submission.jar Group35.class Individual.class Population.class Island.class Archipelago.class Printing.class && java -jar testrun.jar -submission=Group35 -evaluation=SphereEvaluation -seed=1


# Assignment Description

The goal of this assignment is twofold:

1. Design an evolutionary algorithm (EA) that can optimise a given set of functions. Main evaluation criterion: **quality of obtained solutions**.
2. Gain insights on how specific features influence the performance of an EA. Main evaluation criterion: **quality of your analysis**.

## Details

You are required to design and implement an EA to maximise three continuous optimisation problems in 10 dimensions with a limited computational budget:
  * Bent Cigar
  * Katsuura
  * Schaffers F7 function
  

You have to identify one or more EA features to investigate and conduct an experimental study on their effects on EA performance: 
  * novelty search
  * island models
  * parameter settings
    
Please read the "Reporting Experimental Research" document at  for guidelines.
Note that for this assignment you do not need all usual sections of a research report. On five pages you needn't include much on related work, but **clearly formulating a research question or research goal is very important**. 

You have to form groups of four students by subscribing through canvas (deadline: 7th September)
You have to hand in a report (deadline **Sunday 21th of October**) of maximum 5 pages.
Hand in your report through canvas not later than the 21th of October. 
In your report, include a link to your source code (github link). 
Your report and source code will be checked for plagiarism and your code will be tested to compare the performance as stated in the report. 
You have the option (not mandatory) to hand in your EA online to earn 0.5 bonus on your final course grade (note: not on the assignment grade) when you score highest on one of the functions. You can earn a maximum of up to 1 bonus point. For the second place, this bonus is 0.3 and third place 0.1.
Codes to submit your algorithm online will be provided the second week of the course. The online contest will be hosted on: http://mac360.few.vu.nl:8080/EC_BB_ASSIGNMENT/index.html. 
Your algorithm will be tested with different seeds and it will report the average per function over 5 runs. 
The online contest will close on the 20th of October (one day before the assignment deadline).
 

## Programming environment

We have created an environment (in Java) to design and test an evolutionary algorithm. This environment is also suitable for the (optional) online contest.
We implemented a fitness function that scales the performance of your algorithm between 0 and 10.
A score of 0 means that your algorithm scores as good as a random search. A value of 10 means that your algorithm found the global optimum.
The search space is [-5,5]^10 (meaning that every variable must have a value between [-5,5]).
You can check whether the functions have the following properties to adapt your EA in the right way: 
  * whether it is multimodal
  * whether it is separable
  * whether it has a strong structure 
  * the number of available evaluations
 
We tried to make the setup as robust as we could. Nevertheless, working with all different hardware and software platforms, we could still run into some problems. If you run into problems please ask fellow students, double check the tutorial, post questions on the discussion platform, or ask questions during the dedicated lecture on Monday the 10th of September. 

# Great run settings:
- Single island -
SphereEvaluation: sh runSingleIsland.sh 10 SphereEvaluation 20 20 exponential tournament whole-arith non-uniform-ctrl-adap mu,lambda 0.5 5 0.02 0 0.05 1.2
BentCigarFunction: sh runSingleIsland.sh 10 BentCigarFunction 20 20 exponential tournament whole-arith non-uniform-ctrl-adap mu,lambda 0.5 5 0.02 0 0.05 1.2

- Many islands -
BentCigarFunction: sh runManyIslands.sh 10 BentCigarFunction 20 20 exponential tournament whole-arith non-uniform-ctrl-adap replaceWorst 5 45 circle 5 epoch 0.5 5 0.02 0 0.05 1.2
SchaffersEvaluation: sh runManyIslands.sh 10 SchaffersEvaluation 20 20 exponential tournament whole-arith non-uniform-ctrl-adap replaceWorst 5 45 circle 5 epoch 0.5 5 0.02 0 0.05 1.2

