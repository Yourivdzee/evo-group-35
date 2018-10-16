import matplotlib.pyplot as plt
import numpy as np
import sys

class Run(object):
    def __init__(self, attributes):
        self.attributes = attributes

runs = []
attrs = []
with open('runs_summary.txt','r') as file:
    lines = np.array(file.readlines())
    separators = np.where(lines=='-------------------------------------\n')
    
for i in range(len(separators[0]) - 1):
    separator = separators[0][i]
    next_separator = separators[0][i+1]
    attributes = {}
    for line in range(separator+1,next_separator):
        current_line = lines[line]
        if ":" in current_line:
            content = current_line.split(":")
            attribute = content[0].rstrip()
            value = content[1].rstrip()
            if attribute not in attrs:
                attrs.append(attribute)
            attributes[attribute] = value
    
    run = Run(attributes)
    runs.append(run)

# Print headers
print(",".join(attrs))
for run in runs:
    for attr in attrs:
        if attr in run.attributes.keys():
            sys.stdout.write(run.attributes[attr] +",")
            sys.stdout.flush()
        else:
            print("-")

    print("")
