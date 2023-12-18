from collections import defaultdict
import heapq
from math import inf
import numpy as np

HORIZONTALLY = "h"
VERTICALLY = "v"
MIN_STEPS = 4
MAX_STEPS = 10

with open("../inp/17.txt", 'r') as f:
    lines = f.readlines()

matrix_rows = []
for line in lines:
    matrix_rows.append(np.array([int(c) for c in line.strip()]))
m = np.vstack(matrix_rows)
max_row, max_col = [e - 1 for e in m.shape]
target_loc = [max_row, max_col]

to_visit = [
    (0, (0, 0, HORIZONTALLY)),
    (0, (0, 0, VERTICALLY)),
]
heapq.heapify(to_visit)

costs = defaultdict(lambda: inf)
costs[0, 0, HORIZONTALLY] = 0
costs[0, 0, VERTICALLY] = 0

while to_visit:
    this_cost, this_node = heapq.heappop(to_visit)
    row_this, col_this, was_going = this_node

    if this_cost > costs[this_node]:
        continue

    if was_going == HORIZONTALLY:
        for direction in [-1, 1]:
            new_cost = this_cost
            for num_steps in range(1, MAX_STEPS + 1):
                col_new = col_this + direction * num_steps
                if 0 <= col_new <= max_col:
                    new_cost += m[row_this, col_new]
                    if num_steps < MIN_STEPS:
                        continue
                    v = (row_this, col_new, VERTICALLY)
                    if new_cost < costs[v]:
                        costs[v] = new_cost
                        heapq.heappush(to_visit, (new_cost, v))
    else:
        for direction in [-1, 1]:
            new_cost = this_cost
            for num_steps in range(1, MAX_STEPS + 1):
                row_new = row_this + direction * num_steps
                if 0 <= row_new <= max_row:
                    new_cost += m[row_new, col_this]
                    if num_steps < MIN_STEPS:
                        continue
                    v = (row_new, col_this, HORIZONTALLY)
                    if new_cost < costs[v]:
                        costs[v] = new_cost
                        heapq.heappush(to_visit, (new_cost, v))

print(
    min(
        costs[k] for k in costs if k[0] == max_row and k[1] == max_col
    )
)
