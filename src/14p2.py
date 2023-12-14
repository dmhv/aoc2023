import numpy as np


with open("../inp/14.txt", 'r') as f:
    lines = f.readlines()


def char2int(c):
    return 0 if c == "." else 1 if c == "O" else -1


thisMatrixRows = []
for line in lines:
    thisMatrixRows.append(np.array(list(char2int(c) for c in line.strip())))
m = np.vstack(thisMatrixRows)


def tilt(arr: np.array, direction: str) -> np.array:
    if direction == "south":
        tmp = arr[::-1, :]
        tmp = tilt(tmp, "north")
        return tmp[::-1, :]

    if direction == "west":
        tmp = np.transpose(arr)
        tmp = tilt(tmp, "north")
        return np.transpose(tmp)

    if direction == "east":
        tmp = np.transpose(arr)
        tmp = tmp[::-1, :]
        tmp = tilt(tmp, "north")
        tmp = tmp[::-1, :]
        return np.transpose(tmp)

    for c in range(arr.shape[1]):
        cubes_idx = np.where(arr[:, c] == -1)[0]
        if not cubes_idx.any():
            rocks_cnt = np.sum(arr[:, c] == 1)
            arr[0:rocks_cnt, c] = 1
            arr[rocks_cnt:, c] = 0
            continue
        cubes_idx = np.hstack([-1, cubes_idx, arr.shape[1] + 1])
        for i, cube_idx in enumerate(cubes_idx[1:]):
            prev_cube_idx = cubes_idx[i]+1
            rocks_cnt = np.sum(arr[prev_cube_idx:cube_idx, c] == 1)
            first_rock_idx = prev_cube_idx
            last_rock_idx = first_rock_idx + rocks_cnt
            arr[first_rock_idx:last_rock_idx, c] = 1
            arr[last_rock_idx:cube_idx, c] = 0
    return arr


def cycle(arr: np.array) -> np.array:
    arr = tilt(arr, "north")
    arr = tilt(arr, "west")
    arr = tilt(arr, "south")
    arr = tilt(arr, "east")
    return arr


def score(arr):
    out = 0
    for r in range(arr.shape[0]):
        load = arr.shape[1] - r
        rocks_cnt = np.sum(arr[r, :] == 1)
        out += load * rocks_cnt
    return out


scores = []
for i in range(200):
    m = cycle(m)
    scores.append(score(m))

print(scores)
# From this, find the repeated pattern and the value it will be at on step 1000000000. I've done that with a calculator.
