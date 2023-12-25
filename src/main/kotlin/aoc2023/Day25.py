import random
import networkx

with open("../../resources/2023/Day25Input.txt") as f:
    s = f.read().strip()

G = networkx.Graph()

for line in s.split("\n"):
    u, v = line.split(": ")
    v = v.split(" ")
    for vi in v:
        G.add_node(u)
        G.add_node(vi)
        G.add_edge(u, vi, capacity=1.)

cut_value = 0
while cut_value != 3:
    k1 = random.choice(list(G.nodes.keys()))
    k2 = random.choice(list(G.nodes.keys()))
    cut_value, partition = networkx.minimum_cut(G, k1, k2)

print(len(partition[0]) * len(partition[1]))

