#! /usr/bin/python

__author__="sdiemert"
__date__ ="$Jun 13, 2013 8:58:22 AM$"

import PyML as pml
import numpy as np
from decimal import *

SERIES = "./"

class ListSVM:
    def __init__(self, data_file):
        """
        Trains the svm on whatever is the in datafile
        """
        self.train_data = pml.SparseDataSet(data_file, labelsColumn=0)
        self.train_data.attachKernel('gaussian', gamma = 0.5)
        self.svm = pml.SVM()
        self.svm.train(self.train_data)

    def save(self, location):
        self.svm.save(location)

    def test(self, arr, inlines=None, f=None):
        d = pml.SparseDataSet(arr)
        result = self.svm.test(d)
        '''
        if inlines is not None:
            for i,j,k in zip(inlines, result.getDecisionFunction(), result.getPredictedClass()):
                #print Decimal(str(j)).normalize()
                print i,j,k
                if f is not None: f.write(str(k)+" "+str(j)+"\n")
        '''
        return zip(result.getPredictedClass(), np.array(arr).tolist())

if __name__ == "__main__":

    sm = ListSVM("../../test_data_type_1/"+SERIES+"svm_train.data")

    X = np.array([\
        [1,9,0.0,0.307692307692,0.230769230769,0.0,0.0769230769231,0.0,4.84615384615,0.0],\
        [1,8,0.0,0.666666666667,0.0833333333333,0.0,0.0,0.0,4.5,0.0307692307692],\
        [1,1 ,11 ,0.0 ,0.363636363636 ,0.181818181818 ,0.0 ,0.0 ,0.0909090909091 ,4.63636363636 ,0.0]\
    ])
    test_data = pml.SparseDataSet(X)
    j = sm.svm.test(test_data)
    k = j.getPredictedLabels()
    c = j.getPredictedClass()
    p = j.getGivenLabels()

    true_list = [line.split(" ")[0] for line in open("../../test_data_type_1/"+SERIES+"svm_test.data", 'r')]
    total = 0
    correct = 0
    true_count = 0
    true_true_count = 0
    false_pos = 0
    plus_1 = 0
    minus_1 = 0
    for truth, exp in zip(true_list, k):
        total += 1
        if truth == "+1":
            true_true_count += 1
        if truth == exp:
            correct += 1
        if truth == "+1" and truth==exp:
            true_count += 1
        if truth == "-1" and exp == "+1": 
            false_pos += 1
        if exp == "+1":
            plus_1 += 1

    print "Lines classified:",total
    print "Correctly classified lines:",float(correct)/len(true_list), correct,len(true_list)
    print "Correctly classified lines (list):", float(true_count)/true_true_count, true_count, true_true_count
    print "False positives:", float(false_pos)/plus_1, false_pos, plus_1

    print "COMPLETE"
