import os
rootdir = os.getcwd()

for subdir, dirs, files in os.walk(rootdir):
    dir = os.path.join(subdir)
    print(dir)
    os.rename(dir, dir.replace(":","")