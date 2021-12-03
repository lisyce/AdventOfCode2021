file = open("src/three/input.txt").readlines()


final_gamma = ''
final_epsilon = ''

oxy = ''
co = ''

bits = len(file[0]) - 1

def most_least_common_bit(lines, double):
    most_least = ['', '']
    for i in range(bits):
        zero = 0
        one = 0
        for line in lines:
            line = line.strip()
            bit = line[i]
            if int(bit) == 0:
                zero += 1
            else:
                one += 1
        if zero == one and double:
            most_least[0] += '1'
            most_least[1] += '0'
        elif zero > one:
            most_least[0] += '0'
            most_least[1] += '1'
        else:
            most_least[0] += '1'
            most_least[1] += '0'
    return most_least


gamma_epsilon = most_least_common_bit(file, False)
print(gamma_epsilon[0])
print(gamma_epsilon[1])
print()

oxy_list = file.copy()
co_list = file.copy()

for i in range(bits):
    #find most and least common bit in each one
    oxy_check = most_least_common_bit(oxy_list, True)[0]
    co_check = most_least_common_bit(co_list, True)[1]
    
    remove_bits_oxy = []
    remove_bits_co = []
    for line in oxy_list:
        bit = line[i]

        if int(bit) == int(oxy_check[i]):
            pass
        else:
            remove_bits_oxy.append(line)
    for line in co_list:
        bit = line[i]
        if int(bit) == int(co_check[i]):
            pass
        else:
            remove_bits_co.append(line)

    for r in remove_bits_oxy:
        if r in oxy_list and len(oxy_list) > 1:
            oxy_list.remove(r)
    for c in remove_bits_co:
        if c in co_list and len(co_list) > 1:
            co_list.remove(c)


oxy_final = int(oxy_list[0].strip(), 2)
co_final = int(co_list[0].strip(), 2)

print(oxy_final * co_final)