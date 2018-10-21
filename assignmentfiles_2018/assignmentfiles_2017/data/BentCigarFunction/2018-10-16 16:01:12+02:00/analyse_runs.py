import sys
import os
import math

directory = sys.argv[1]
scores = []
times = []

for filename in os.listdir(directory):
    if filename.endswith(".txt"):
        with open(directory + "/" + filename) as myfile:
            first_seven = list(myfile)[1:6]
        with open(directory + "/" + filename) as myfile:
            last_two = list(myfile)[-2:]
            score = float(last_two[-2].split(" ")[1])
            time = float(last_two[-1].split(" ")[1].split("ms")[0])
            scores.append(score)
            times.append(time)

mean = sum(scores)/len(scores)

sum_squares_scores = 0
for score in scores:
    sum_squares_scores += (mean - score)**2


mean_time = sum(times)/len(times)
sum_squares_times = 0
for time in times:
    sum_squares_times += (mean_time - time)**2
    
with open("runs_summary.txt", "a") as summary_file:
    summary_file.write("-------------------------------------\n")
    for line in first_seven:
        summary_file.write(line)
    summary_file.write("Runs: {} \n".format(len(scores)))
    summary_file.write("Mean fitness: {}\n".format(mean))
    summary_file.write("Max fitness: {}\n".format(max(scores)))
    summary_file.write("Standard deviation of scores: {} \n".format(math.sqrt(sum_squares_scores/len(scores))))
    summary_file.write("Mean time: {}ms\n".format(mean_time))
    summary_file.write("Standard deviaton of times: {}\n".format(math.sqrt(sum_squares_times/len(times))))

            