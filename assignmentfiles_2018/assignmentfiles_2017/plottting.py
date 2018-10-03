# ploting for archipelago and islands fitness statistics (max, mean, std)
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

#list with the generatios where migration happened
exchange_generations = df.index[(df.IslandId == 0) & (df.Exchange == True)].tolist()

# color dictionary for different islands
# max 8 for now
color_dict = ["r", "b", "g", "c", "m", "y", "k", "w"]

# plotting 
# blah blah blah
f, ax = plt.subplots(nrows = 1, ncols = 3, sharex = False, sharey=True, figsize = [20, 5])

for i in range(island_num + 1):
    
    if i == 0:
        l = "Archipelago"
    else:
        l = "Island " + str(i)
        
    ax[0].plot(df.Maximum[df.IslandId == i], color = color_dict[i], label = l, linewidth = 1, linestyle = '-')
    ax[1].plot(df.Average[df.IslandId == i], color = color_dict[i], label = l, linewidth = 1, linestyle = '-')
    ax[2].plot(df.StandardDev[df.IslandId == i], color = color_dict[i], label = l, linewidth = 1, linestyle = '-')

ax[0].set_title('Maximum')
ax[1].set_title('Average')
ax[2].set_title('StandardDev')

plt.legend()

# save figure in directory
plt.savefig(dir_loc + "plots.png", dpi = 600)