file = open("src/day3/input.txt").readlines()

#gamma
final_gamma = ''
final_epsilon = ''

bits = len(file[0]) - 1


for i in range(bits):
    zero = 0
    day1 = 0
    for line in file:
        line = line.strip()
        bit = line[i]
        if int(bit) == 0:
            zero += 1
        else:
            day1 += 1
    if zero > day1:
        final_gamma += '0'
        final_epsilon += '1'
    else:
        final_gamma += '1'
        final_epsilon += '0'

print(int(final_gamma, 2) * int(final_epsilon, 2))