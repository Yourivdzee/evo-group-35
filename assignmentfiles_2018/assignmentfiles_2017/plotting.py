# plotting for archipelago and islands fitness statistics (max, mean, std)
# name of the file containing the printed data is output.log

import matplotlib.pyplot as plt
import matplotlib.cm as cm
import pandas as pd
import numpy as np

dir_loc = ""
file_name = "output.log"

# read log file and delete last two lines that are score and time
df = pd.read_table(dir_loc + file_name, sep = " ", index_col = "Generation", error_bad_lines = False)
df = df[:-2]

# correct type of IslandId from str to int
df["IslandId"] = df["IslandId"].astype(int)

# count the number of islands
island_num = df.IslandId.max()

# number of generations
gen_num = int(df.index[-1])

#list with the generatios where migration happened
exchange_generations = df.index[(df.IslandId == 0) & (df.Exchange == True)].tolist()

# color dictionary for different islands
# max 8 for now
color_dict = ["k", "r", "b", "g", "c", "m", "y", "w"]

# plotting 
# blah blah blah
f, ax = plt.subplots(nrows = 3, ncols = 1, sharex = True, sharey = False, figsize = [25, 8])

for i in range(island_num + 1):
    
    if i == 0:
        l = "Archipelago"
        lw = 1.5
        ls = "-"
        a = 0.9
    else:
        l = "Island " + str(i)
        lw = 1
        ls = "-"
        a = 0.5
        
    ax[0].plot(df.Maximum[df.IslandId == i], color = color_dict[i], label = l, linewidth = lw, linestyle = ls, alpha = a)
    ax[1].plot(df.Average[df.IslandId == i], color = color_dict[i], label = l, linewidth = lw, linestyle = ls, alpha = a)
    ax[2].plot(df.StandardDev[df.IslandId == i], color = color_dict[i], label = l, linewidth = lw, linestyle = ls, alpha = a)

ax[0].set_title('Maximum')
ax[1].set_title('Average')
ax[2].set_title('StandardDev')

ax[2].set_xticks(np.arange(0, gen_num + 2, (gen_num + 2) // 5))

for i in range(len(ax)):
	ax[i].axvline(int(df.index[df.Maximum == df.Maximum.max()][0]), ymin = -1, ymax =1, color = "k", linestyle = '-.', linewidth = 1, clip_on = False, label = "Max")

plt.legend(bbox_to_anchor = [1.1, 1.1])

# save figure in directory
plt.savefig(dir_loc + "plots.png", dpi = 300)

print("figures saved at plots.png")