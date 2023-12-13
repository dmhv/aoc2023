import numpy as np
import math

with open("../inp/13.txt", 'r') as f:
    lines = f.readlines()


def char2int(c):
    return 0 if c == "." else 1


def find_split_by_axis(arr: np.array, axis: int, ignore_row: int) -> int:
    if axis == 1:
        arr = np.transpose(arr)

    n = math.ceil(arr.shape[0] / 2)
    for i in range(1, n):
        if i == ignore_row:
            continue
        top = arr[:i, :]
        bot = arr[(2 * i - 1):(i - 1):-1, :]
        if np.all(top == bot):
            return i

    arf = arr[::-1, :]
    for i in range(1, n):
        if arr.shape[0] - i == ignore_row:
            continue
        top = arf[:i, :]
        bot = arf[(2 * i - 1):(i - 1):-1, :]
        if np.all(top == bot):
            return arr.shape[0] - i

    return -1


ms = []
thisMatrixRows = []
for line in lines:
    if not line.strip():
        ms.append(np.vstack(thisMatrixRows))
        thisMatrixRows = []
    else:
        thisMatrixRows.append(np.array(list(char2int(c) for c in line.strip())))

sum_row_splits = 0
sum_col_splits = 0

for m in ms:
    initial_row_split = find_split_by_axis(m, 0, -1)
    initial_col_split = find_split_by_axis(m, 1, -1)
    stop = False

    for r in range(m.shape[0]):
        if stop:
            break
        for c in range(m.shape[1]):
            mc = np.copy(m)
            mc[r, c] = 1 if m[r, c] == 0 else 0

            new_row_split = find_split_by_axis(mc, 0, initial_row_split)
            if new_row_split > 0:
                sum_row_splits += new_row_split
                stop = True
                break

            new_col_split = find_split_by_axis(mc, 1, initial_col_split)
            if new_col_split > 0:
                sum_col_splits += new_col_split
                stop = True
                break

out = 100 * sum_row_splits + sum_col_splits
print(out)
