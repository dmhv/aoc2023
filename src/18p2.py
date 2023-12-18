with open("../inp/18.txt", 'r') as f:
    lines = f.readlines()

r = 0
c = 0
points = [(r, c)]
boundary_points = 0
for line in lines:
    hex_value = line.split(" ")[2].strip("()#\n")
    d = hex_value[-1]
    cnt = int(hex_value[:-1], 16)
    if d == "0":
        c += int(cnt)
    elif d == "2":
        c -= int(cnt)
    elif d == "1":
        r += int(cnt)
    else:
        r -= int(cnt)
    points.append((r, c))
    boundary_points += int(cnt)

area = 0
for (p1, p0) in zip(points[1:], points[:-1]):
    area += (p0[0] + p1[0]) * (p0[1] - p1[1])
area = abs(area) // 2

interior_points = area - boundary_points // 2 + 1
print(interior_points + boundary_points)
