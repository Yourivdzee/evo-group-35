import sys
import os

directory = sys.argv[1]
scores = []
times = []

for filename in os.listdir(directory):
    if filename.endswith(".txt"):
        with open(directory + "/" + filename) as myfile:
            last_two = list(myfile)[-2:]
            score = float(last_two[-2].split(" ")[1])
            time = float(last_two[-1].split(" ")[1].split("ms")[0])
            scores.append(score)
            times.append(time)

mean = sum(scores)/len(scores)
print("Runs: {}".format(len(scores)))
print("Mean fitness: {}".format(mean))
print("Max fitness: {}".format(max(scores)))

sum_squares = 0
for score in scores:
    sum_squares += (mean - score)**2

print("Standard deviation of scores: {} \n".format(sum_squares/len(scores)))

mean_time = sum(times)/len(times)
print("Mean time: {}ms".format(mean_time))

sum_squares = 0
for time in times:
    sum_squares += (mean_time - time)**2
print("Standard deviaton of times: {}".format(sum_squares/len(times)))

        