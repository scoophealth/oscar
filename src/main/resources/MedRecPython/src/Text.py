#! /usr/bin/python
__author__="sdiemert"
__date__ ="$May 31, 2013 1:26:31 PM$"

import nltk
import Database
import MedFreqDist
import re
import os
import tempfile
os.environ['MPLCONFIGDIR'] = tempfile.mkdtemp()
import matplotlib.pyplot as plt
import DrugFinder
import numpy as np
import DBScan

class Text(object):
    def __init__(self, raw_text, medications=list()):
        self._sent_tokenizer = self._teach_abbrevations()
        self._tagged_sents = None
        self._lines = None
        self._raw_text = raw_text
        self.medications = medications
        self.lists = list()

    def addList(l):
        self.lists.append(l)

    def _teach_abbrevations(self,abbrevation_file=None):
        if abbrevation_file is None:
            f = open("/vagrant/Medications/resources/abbrev_file.csv", "r")
        else:
            f = open(abbrevation_file, 'r')
        
        training_list = list()
        for word in f.read().split(", "):
            training_list.append(word)
        
        params = nltk.tokenize.punkt.PunktParameters()
        params.abbrev_types = set(training_list)
        self.sent_splitter = nltk.tokenize.punkt.PunktSentenceTokenizer(params)
        #print "INFO:  Custom sentence tokenizer initalized and taught medical abrrevations."
        return self.sent_splitter

    def getRawText(self):
        return self._raw_text

    def getLines(self, r=None):
        if self._lines:
            if r is not None:
                return self._lines[r[0]:r[1]+1]
            else:
                return self._lines
        else:
            if r is not None:
                self._lines = [line for line in self._raw_text.split('\n')][r[0]:r[1]+1]
            else:
                self._lines = [line for line in self._raw_text.split('\n')]
            return self._lines

    def getSentences(self):
        return self._sent_tokenizer.tokenize(self._raw_text)

    def getTaggedSentences(self):
        return_list = list()
        if self._tagged_sents:
            return self._tagged_sents
        else:
            for s in self.getSentences():
                return_list.append(nltk.pos_tag(nltk.word_tokenize(s)))
            self._tagged_sents = return_list
            return return_list

    def getTaggedLines(self):
        return [nltk.pos_tag(nltk.word_tokenize(s)) for s in self.getLines()]

    def getSingleString(self):
        t = this.getSentences()
        return ' '.join(t)

    def getString(self, line_ends = False):
        if line_ends:
            return ' '.join([s.rstrip() for s in self.getLines()])
        else:
            return self.getRawText()

    def getMedFreqDist(self, meds=None):
        if not self.medications:
            self.meds = self.findMeds()
            meds = self.meds
        elif meds is None:
            meds = self.findMeds()
        mdf = MedFreqDist.MedFreqDist(self, meds)
        return mdf.percentileMedDist()

    def findMeds(self):
        if not self.medications:
            db = Database.Database("127.0.0.1","root","05sc@r","drugref")
            df = DrugFinder.DrugFinder(self, db)
            return df.findDrugs()
        else:
            return self.medications

    def determineLineDistance(self, words, percent=False):
        """
        Determines the number of lines that seperate all the lines
        in the text from words in the list of words.  Words in the list
        of words MUST all occur on the same line for them to match.
        """
        distance_dist = list()
        kw_lines = self._findKeywordLines(words, 2)
        i = 0
        for line in self.getLines():
            temp_list = list()
            for l in kw_lines:
                if i<l:
                    continue
                else:
                    temp_list.append(abs(i-l))
            if len(temp_list)>0: closest = min(temp_list)
            else: closest = -1
            if not percent:
                distance_dist.append(float(closest)/len(self.getLines()))
            else:
                distance_dist.append(closest)
            i+=1
        
        return distance_dist

    def determineWordLengthDist(self):
        return_list = list()
        for line in self.getLines():
            j = 0
            s = 0
            for word in nltk.word_tokenize(line):
                s += len(word)
                j += 1
            if j == 0:
                return_list.append(0)
            else:
                k = float(s)/j
                return_list.append(k)
        return return_list

    def _findKeywordLines(self, words, key_match_threshold):
        count = 0
        return_list = list()
        for line in self.getLines():
            if len([w for w in words if re.match(".*"+w.upper()+".*", line.upper())]) >= key_match_threshold:
                return_list.append(count)
            count += 1
        return return_list

    def determinePosFreq(self, pos_type):
        return_list = list()
        for line in self.getTaggedLines():
            j = 0
            for word, pos in line:
                if pos_type in pos:
                    j += 1
            if len(line) == 0: return_list.append(0.0)
            else:
                k = float(j)/len(line)
                return_list.append(k)
        return return_list

    def acronymFreq(self):
        return_list = list()
        for line in self.getLines():
            line_count = 0
            for word in nltk.word_tokenize(line):
                if re.match("^p\.?o\.?$", word, re.IGNORECASE):
                    #print word
                    line_count += 1
                elif re.match("^[tbq]\.?i\.?d\.?$", word, re.IGNORECASE):
                    #print word
                    line_count += 1
                elif re.match("^q\.?[0-9]{1,2}\.?h$", word, re.IGNORECASE):
                    #print word
                    line_count += 1
                elif re.match("^q\.?d$", word, re.IGNORECASE):
                    #print word
                    line_count += 1
                elif re.match("^p\.?r\.?n\.$", word, re.IGNORECASE):
                    #print word
                    line_count += 1
                    
            if len(line) == 0: return_list.append(0)
            else: return_list.append(float(line_count)/len(line))
        return return_list

    def getListRange(self, svm):
        #text_values = zip(line_dist, mfd, noun_dist, verb_dist, adj_dist, art_dist, con_dist, word_length_dist, ac_dist)
        test_list = zip(\
            [0]*len(self.getLines()),
            #self.determineLineDistance(["DISCHARGE","MEDICATION","INSTRUCTION"], percent=True),
            self.getMedFreqDist(),
            self.determinePosFreq("NN"),
            self.determinePosFreq("VB"),
            self.determinePosFreq("JJ"),
            self.determinePosFreq("DT"),
            self.determinePosFreq("CC"),
            self.determineWordLengthDist(),
            self.acronymFreq()
        )
        tl = list()
        for a,c,d,e,f,g,h,i,j in test_list:
           l = [a,c,d,e,f,g,h,i,j]
           tl.append(l)

        #f = open('../resources/results/svm_scores.csv','a');
        result = svm.test(np.array(tl), inlines=self.getLines())
        #f.close(a)
        return result 

    def cluster(self, dist, eps=3, minpoints=3):
        dbs = DBScan.DBScan(eps, minpoints, dist)
        clusters = dbs.findClusters()
        clusters = dbs.analyzeCluster(clusters)
        list_range = self.checkListTitle(clusters,["DISCHARGE","MEDICATION","INSTRUCTION"])
        return list_range
    
    def checkListTitle(self,clusters, keywords):
        return_list = list()
        for c in clusters:
            lines = self.getLines(r=(min(c)-2,max(c)))
            for line in lines:
                if len([w for w in keywords if re.match(".*"+w.upper()+".*", line.upper())]) >= 2:
                    return_list.append(c)    
                    break
        return return_list



if __name__ == "__main__":
    print "Starting Text.py file execution.";

    tf = open("../../test_data_type_1/KEY","r")
    tf = [line.split(", ") for line in tf]
    tf = [[i.rstrip(), j.rstrip(), k.rstrip()] for i,j,k in tf]

    x1 = list()
    x2 = list()
    y1 = list()
    y2 = list()

    count = 0
    for f,s,e in tf:
        #if count > 2: break
        print f
        f = open("../../test_data_type_1/"+f, 'r')
        text = Text(f.read())
        true_range = range(int(s), int(e)+1)
        q =  text.acronymFreq()
        r = text.determinePosFreq("JJ")
        x1 += [q[i] for i in range(len(q)) if i in true_range]
        y1 += [r[i] for i in range(len(r)) if i in true_range]
        x2 += [q[i] for i in range(len(q)) if i not in true_range]
        y2 += [r[i] for i in range(len(r)) if i not in true_range]
        count += 1
    print len(x1) + len(x2)

    #plt.plot(x2,y2,"bo", x1,y1,"ro")


    y1.sort()
    y2.sort()

    x1_mean = float(sum(x1))/len(x1)
    y1_mean = float(sum(y1))/len(y1)
    x2_mean = float(sum(x2))/len(x2)
    y2_mean = float(sum(y2))/len(y2)

    fig = plt.figure(1)
    fig.suptitle("Line Distance v.s JJ Freq")
    plt.subplot(211)
    plt.plot(x2,y2,"bo", x2,[y2_mean for i in x2], [x2_mean for i in y2], y2)
    plt.title("Not in list")
    plt.xlabel("Acronym Freq")
    plt.ylabel("JJ Dist")
    plt.subplot(212)
    plt.plot(x1,y1,"ro", x1,[y1_mean for i in x1], [x1_mean for i in y1], y1)
    plt.title("In list")
    plt.xlabel("Acronym Freq")
    plt.ylabel("JJ Dist")
    plt.show()
