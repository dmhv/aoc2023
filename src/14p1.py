import numpy as np

with open("../inp/14.txt", 'r') as f:
    lines = f.readlines()


def char2int(c):
    return 0 if c == "." else 1 if c == "O" else -1


thisMatrixRows = []
for line in lines:
    thisMatrixRows.append(np.array(list(char2int(c) for c in line.strip())))
m = np.vstack(thisMatrixRows)

for c in range(m.shape[1]):
    cubes_idx = np.where(m[:, c] == -1)[0]
    cubes_idx = np.hstack([-1, cubes_idx, m.shape[1] + 1])
    for i, cube_idx in enumerate(cubes_idx[1:]):
        prev_cube_idx = cubes_idx[i] + 1
        rocks_cnt = np.sum(m[prev_cube_idx:cube_idx, c] == 1)
        first_rock_idx = prev_cube_idx
        last_rock_idx = first_rock_idx + rocks_cnt
        m[first_rock_idx:last_rock_idx, c] = 1
        m[last_rock_idx:cube_idx, c] = 0

out = 0
for r in range(m.shape[0]):
    load = m.shape[1] - r
    rocks_cnt = np.sum(m[r, :] == 1)
    out += load * rocks_cnt

print(out)
