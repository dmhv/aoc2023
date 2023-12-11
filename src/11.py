import numpy as np

with open("11.txt", 'r') as f:
    lines = f.readlines()

mul = 1000000


def distance(a, b):
    return np.sum(np.abs(np.longlong(a) - np.longlong(b)))


def adjust_indices(lst, component):
    lst.sort(key=lambda e: e[component])
    xs = [e[component] for e in lst]
    dx = [a - b for a, b in zip(xs[1:], xs[:-1])]
    dx = [a if a < 2 else mul * (a - 1) + 1 for a in dx]
    adjusted_indices = np.cumsum([xs[0]] + dx)

    for i in range(len(lst)):
        lst[i][component] = adjusted_indices[i]
    return lst


locs = []
for (r, l) in enumerate(lines):
    for (c, s) in enumerate(l.strip()):
        if s == "#":
            locs.append([r, c])

for component, _ in enumerate(locs[0]):
    locs = adjust_indices(lst=locs, component=component)

numGalaxies = len(locs)
distances = np.zeros(numGalaxies * (numGalaxies - 1), dtype="int64")
idx = 0
for i, n1 in enumerate(locs):
    for n2 in locs[(i + 1):]:
        distances[idx] = distance(n1, n2)
        idx += 1

print(np.sum(distances))
