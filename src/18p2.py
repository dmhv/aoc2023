with open("../inp/18tiny.txt", 'r') as f:
    lines = f.readlines()

r = 0
c = 0
points = [(r, c)]
cnt_boundary_points = 0
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
    cnt_boundary_points += int(cnt)

area = 0
for (p1, p0) in zip(points[1:], points[:-1]):
    area += (p0[0] + p1[0]) * (p0[1] - p1[1])
area = abs(area) // 2

print("Area (Shoelace formula) is", area)
print("Count of boundary points is", cnt_boundary_points)
cnt_interior_points = area - cnt_boundary_points // 2 + 1
print("Count of interior points (Pick's theorem) is", cnt_interior_points)
print("Interior + boundary is", cnt_interior_points + cnt_boundary_points)
