file = open("src/day01/input.txt")

total_increases = 0
last_total = 0
data_set = [-1, int(file.readline().strip()), int(file.readline().strip())]


this_total = 0

data_set[0] = data_set[1]
data_set[1] = data_set[2]
data_set[2] = int(file.readline())

for data in data_set:
    this_total += data
if last_total != 0 and this_total > last_total:
    total_increases += 1
last_total = this_total

print(total_increases)