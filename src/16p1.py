from typing import List, Tuple
from collections import deque, defaultdict

import numpy as np

with open("../inp/16.txt", 'r') as f:
    lines = f.readlines()


LEFT = "left"
RIGHT = "right"
UP = "up"
DOWN = "down"


def i2c(c):
    return "." if c == 0 else "/" if c == 1 else "\\" if c == 2 else "|" if c == 3 else "-"


def c2i(c):
    return 0 if c == "." else 1 if c == "/" else 2 if c == "\\" else 3 if c == "|" else 4


def get_neighbors(r: int, c: int, direction_from: str, m: np.array) -> List[Tuple[int, int, str]]:
    nr, nc = m.shape

    if direction_from == LEFT:
        if i2c(m[r, c]) in ".-" and c < nc-1: return [(r, c+1, LEFT)]
        if i2c(m[r, c]) == "\\" and r < nr-1: return [(r+1, c, UP)]
        if i2c(m[r, c]) == "/" and r > 0: return [(r-1, c, DOWN)]
        if i2c(m[r, c]) == "|":
            out = []
            if r > 0: out.append((r-1, c, DOWN))
            if r < nr-1: out.append((r+1, c, UP))
            return out

    if direction_from == RIGHT:
        if i2c(m[r, c]) in ".-" and c > 0: return [(r, c-1, RIGHT)]
        if i2c(m[r, c]) == "/" and r < nr-1: return [(r+1, c, UP)]
        if i2c(m[r, c]) == "\\" and r > 0: return [(r-1, c, DOWN)]
        if i2c(m[r, c]) == "|":
            out = []
            if r > 0: out.append((r-1, c, DOWN))
            if r < nr-1: out.append((r+1, c, UP))
            return out

    if direction_from == UP:
        if i2c(m[r, c]) in ".|" and r < nr-1: return [(r+1, c, UP)]
        if i2c(m[r, c]) == "\\" and c < nc-1: return [(r, c+1, LEFT)]
        if i2c(m[r, c]) == "/" and c > 0: return [(r, c-1, RIGHT)]
        if i2c(m[r, c]) == "-":
            out = []
            if c > 0: out.append((r, c-1, RIGHT))
            if c < nc-1: out.append((r, c+1, LEFT))
            return out

    if direction_from == DOWN:
        if i2c(m[r, c]) in ".|" and r > 0: return [(r-1, c, DOWN)]
        if i2c(m[r, c]) == "/" and c < nc-1: return [(r, c+1, LEFT)]
        if i2c(m[r, c]) == "\\" and c > 0: return [(r, c-1, RIGHT)]
        if i2c(m[r, c]) == "-":
            out = []
            if c > 0: out.append((r, c-1, RIGHT))
            if c < nc-1: out.append((r, c+1, LEFT))
            return out

    return []


thisMatrixRows = []
for line in lines:
    thisMatrixRows.append(np.array(list(c2i(c) for c in line.strip())))
m = np.vstack(thisMatrixRows)


to_visit = deque()
to_visit.append((0, 0, LEFT))
visited = set()
count_visited = defaultdict(int)

while to_visit:
    e = to_visit.pop()
    visited.add(e)
    r, c, direction = e
    count_visited[(r, c)] += 1
    neighbours = get_neighbors(r, c, direction, m)
    for n in neighbours:
        if n not in visited:
            to_visit.append(n)

print(len(count_visited.keys()))
