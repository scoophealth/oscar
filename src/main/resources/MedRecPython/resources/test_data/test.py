import matplotlib.pyplot as plt
import nltk

f = open("163611_marked","r")

l = [line for line in f]
f.close()

l1 = [line.split("$$") for line in l]

values = [int(line[0]) for line in l1]
chars = [line[1] for line in l1]
line_len_by_words = [len(nltk.word_tokenize(line[1])) for line in l1]

avg_word_length = list()
for line in l1:
    count = 0
    v = nltk.word_tokenize(line[1])
    for word in v:
        count += len(word)

    if len(v) != 0: avg_word_length.append(float(count)/len(v))
    else: avg_word_length.append(0)
        

lens = avg_word_length

for value, char, length in zip(values, chars, lens):
    print value,length, char

plt.plot(lens, values, "ro")
plt.ylim(-1,2)
plt.show()
