import nltk.corpus
import nltk
import pdb
import Text
import Database
import re

class DrugFinder:
    def __init__(self, text, db):
        if type(text) is str:
            self.text = Text(text)
        else:
            self.text = text
        self.db = db
        self.illegal_chars_list = ["prn","\(", '\)', 'po', 'mg', 'mcg','qd', '\]', '\[', 'bid','tid', 'md','q[0-9]{,3}h']
        self.corpus_black_list = [word.upper() for word in nltk.corpus.brown.words() if word.upper() not in ["ASPIRIN", "PREDNISONE","INSULIN","IRON"]]+["ESCRIPTION"]

    def findDrugs(self):
        nouns = self.find_nouns()  #a list of lists (one list per line) of nouns.
        filtered_nouns = self.filter_common_words(nouns)  #filter out common words from a corpus of words
        filtered_nouns = self.filter_db(filtered_nouns)
        self.db.close()
        return filtered_nouns

    def filter_db(self, nouns):
        return_list = list()
        for l in nouns:
            for words, count in l:
                phrase = " ".join(words)
                if self.db.search_drugref(phrase):
                    return_list.append((phrase,count))
                    #return_list.append(phrase)
        return return_list

    def filter_common_words(self, nouns):
        return_list = list()
        for line in nouns:
            phrase_list = list()
            for phrase, count in line:
                phrase_len = len(phrase)
                c_score = 0
                for word in phrase:
                    if word[-1] in [".", ':',',']: word = word[:-1]
                    if word.upper() in self.corpus_black_list:
                        c_score += 1
                score = (float(c_score)/float(phrase_len))*100
                if score <= 50:
                    #phrase_list.append(phrase)
                    phrase_list.append((phrase,count))
            return_list.append(phrase_list)
        return return_list

    def find_nouns(self):
        return_list = list()
        line_count = 0 
        for line in self.text.getTaggedLines():
            line_list = list()
            index_list = list()
            flag = False
            i = 0
            for word, pos in line:
                word = ''.join(word.split('.'))
                if [word for c in self.illegal_chars_list if re.match("^"+c+"$", word.lower()) is not None] or len(word) <= 2 and flag:
                    flag = False
                elif len(word) <= 3 or [word for c in self.illegal_chars_list if re.match("^"+c+"$", word.lower()) is not None] and not flag:
                    pass
                elif "NN" in pos and not flag:
                    index_list.append(i)
                    flag = True
                elif "NN" in pos and flag:
                    index_list.append(i)
                elif "JJ" in pos and not flag:
                    index_list.append(i)
                    flag = True
                elif "JJ" in pos or "RB" in pos and flag:
                    index_list.append(i)
                elif flag:
                    #no nouns or adjectives anymore.
                    flag = False
                i += 1 
            #pdb.set_trace()
            q = 0
            while index_list:
                i = index_list[q]
                words = []
                k = 0
                for j,k in zip(index_list[q:], range(len(index_list[q:]))):
                    if i+k == j:
                        words.append((line[j], j)) 
                    else:
                        break
                for w, c in words:
                    index_list.remove(c)
                #if words: print words
                words = [word for (word, pos), i in words]
                line_list.append((words,line_count))
                #line_list.append(words)
            return_list.append(line_list)
            line_count += 1
        return return_list



                

if __name__ == "__main__":
    text = open('../resources/test_data/163611', "r").read()
    df = DrugFinder(text, Database("127.0.0.1","root","05sc@r","drugref"))
    df.findDrugs()
