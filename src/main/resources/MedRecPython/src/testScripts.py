#! /usr/bin/python

import matplotlib.pyplot as plt

__author__="sdiemert"
__date__ ="$Jun 12, 2013 9:52:43 AM$"

class TestScripts:
    def __init__(self):
        pass
    def test2(self):
        """
        Change the POS on the text.determinePosFreq() to generate different 
        pos types.
        """
        print "POS v.s. average word length";


        tf = open("../../test_data_type_1/KEY","r")
        tf = [line.split(", ") for line in tf]
        tf = [[i.rstrip(), j.rstrip(), k.rstrip()] for i,j,k in tf]

        x1 = list()
        x2 = list()
        y1 = list()
        y2 = list()

        count = 0
        for f,s,e in tf:
            if count > 10: break
            print f
            f = open("../../test_data_type_1/"+f, 'r')
            text = Text(f.read())
            true_range = range(int(s), int(e)+1)
            q =  text.determinePosFreq("DT")
            r = text.determineWordLengthDist()
            x1 += [q[i] for i in range(len(q)) if i in true_range]
            y1 += [r[i] for i in range(len(r)) if i in true_range]
            x2 += [q[i] for i in range(len(q)) if i not in true_range]
            y2 += [r[i] for i in range(len(r)) if i not in true_range]
            count += 1
        print len(x1) + len(x2)
        plt.plot(x2,y2,"bo", x1,y1,"ro")
        plt.xlabel("freq of part of speech")
        plt.ylabel("average word length")
        plt.title("Freq of parts of speech v.s. average wordl length")
        plt.show()

    def test1(self):
        print "Testing line distance from keywords and average word length";

        tf = open("../../test_data_type_1/KEY","r")
        tf = [line.split(", ") for line in tf]
        tf = [[i.rstrip(), j.rstrip(), k.rstrip()] for i,j,k in tf]

        x1 = list()
        x2 = list()
        y1 = list()
        y2 = list()

        for f,s,e in tf:
            print f
            f = open("../../test_data_type_1/"+f, 'r')
            text = Text(f.read())
            true_range = range(int(s), int(e)+1)
            q =  text.determineLineDistance(["DISCHARGE", "MEDICATION","INSTRUCTION"])
            r = text.determineWordLengthDist()
            x1 += [q[i] for i in range(len(q)) if i in true_range]
            y1 += [r[i] for i in range(len(r)) if i in true_range]
            x2 += [q[i] for i in range(len(q)) if i not in true_range]
            y2 += [r[i] for i in range(len(r)) if i not in true_range]
        print len(x1) + len(x2)
        plt.plot(x1, y1,"ro",x2 ,y2,"bo")
        plt.xlabel("Line distance from Keywords (DISCHARGE, MEDICATION)")
        plt.ylabel("Average Length of words on line.")
        plt.title("Line distance from keywords v.s avg word length by line")
        plt.show()


if __name__ == "__main__":
    test1()