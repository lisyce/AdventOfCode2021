file = open("src/two/input.txt").readlines()

horizontal = 0;
vertical = 0;
aim = 0;

for line in file:
    line = line.strip()
    directions = line.split()
    directions[1] = int(directions[1])
    if directions[0] == 'forward':
        horizontal += directions[1]
        vertical += aim * directions[1]
    elif directions[0] == 'down':
        aim += directions[1]
    elif directions[0] == 'up':
        aim -= directions[1]

print(horizontal * vertical)