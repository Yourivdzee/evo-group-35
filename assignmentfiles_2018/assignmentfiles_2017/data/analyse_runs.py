import sys
import os
import math
rootdir = os.getcwd()
function = sys.argv[1]
run_type =  sys.argv[2]
rootdir2 = rootdir + "/{}/{}".format(function,run_type)
save_loc = rootdir + "/" "{}_ofType_{}.txt".format(function, run_type)
print(rootdir2)
for foldername in os.listdir(rootdir2):
    try:
        scores = []
        times = []
        foldername = rootdir2 + "/" +foldername
        for filename in os.listdir(foldername):
            print(filename)
            if filename.endswith(".txt"):
                print("opened file {}".format(filename))
                this_file = foldername + "/"+ filename
                with open(this_file) as myfile:
                    first_seven = list(myfile)[0:15]
                with open(this_file) as myfile:
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
            
        with open(save_loc, "a") as summary_file:
            summary_file.write("-------------------------------------\n")
            summary_file.write("Filename: " + foldername  + "\n")
            for line in first_seven:
                summary_file.write(line)

            summary_file.write("Runs: {} \n".format(len(scores)))
            summary_file.write("Mean fitness: {}\n".format(mean))
            summary_file.write("Max fitness: {}\n".format(max(scores)))
            summary_file.write("Standard deviation of scores: {} \n".format(math.sqrt(sum_squares_scores/len(scores))))
            summary_file.write("Mean time: {}ms\n".format(mean_time))
            summary_file.write("Standard deviaton of times: {}\n".format(math.sqrt(sum_squares_times/len(times))))

    except (ValueError,IndexError, OSError,ZeroDivisionError) as err:
        continue

print("SAVING AT {}".format(save_loc))