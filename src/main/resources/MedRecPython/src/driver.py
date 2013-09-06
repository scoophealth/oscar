import os
import os.path
#! /usr/bin/python

__author__="sdiemert"
__date__ ="$Jun 14, 2013 1:58:33 PM$"

import Text
import ListSVM
import List


def testClustering(tf_file,svm):
    tf = [line.split(", ") for line in tf_file]
    tf = [[i.rstrip(), int(j.strip().rstrip()), int(k.strip().rstrip())] for i,j,k in tf]
    tf_file.close()

    return_dict = {} 
    
    count = 0 
    for fname, s,e in tf:
        if count > 60: break
        elif count < 35:
            count += 1
            continue
        print "INFO: Starting "+fname,
        print str(100*(float(count)/85))+"% complete"
        f = open("/vagrant/Medications/resources/test_data_type_1/"+fname, 'r')
        text = Text.Text(f.read())
        r = text.getListRange(svm)
        q = text.cluster([i[0] for i in r], eps=3, minpoints=3)
    
        if len(q) <1:
            exp_s = -1
            exp_e = -1
        elif len(q) > 1:
            exp_s = min(q[-1])
            exp_e = max(q[-1])
        else:
            exp_s = min(q[0])
            exp_e = max(q[0])

        exp_t = (exp_s, exp_e)
        true_t = (s,e)

        print "(exp_s, exp_e),(s,e)", exp_t, true_t

        return_dict[fname] = (exp_t, true_t)
        count += 1

    return return_dict
        
def analyzeClusterResults(rfile):
    tf = [line.split(", ") for line in rfile]
    correct_count = 0
    total_exp_count = 0
    total_true_count = 0 
    print "(true),(experimental)"
    for fname, ts, te, es, ee in tf:

        ts = eval(ts.rstrip().strip())
        te = eval(te.rstrip().strip())
        ee = eval(ee.rstrip().strip())
        es = eval(es.rstrip().strip())

        if ts is not None:
            ts = int(ts)
        else:
            ts = -1
        if te is not None:
            te = int(te)
        else:
            te = -1
        if ee is not None:
            ee = int(ee)
        else:
            ee = -1
        if es is not None:
            es = int(es)
        else:
            es = -1

        print (ts,te),(es,ee)
        if (ee == -1 and es == -1) and (te != -1 and ts != -1):
            total_true_count += len(range(ts,te+1))
        elif (te == -1 and ts == -1) and (ee != -1 and es != -1):
            total_exp_count += len(range(es, ee+1))
        elif (te == -1 and ts == -1) and (ee == -1 and es == -1):
            pass
        else:
            total_true_count += len(range(ts,te+1))
            for i in range(es,ee+2):
                if i in range(ts, te+1):
                    correct_count += 1
                total_exp_count += 1


    print "====================="
    print "Precision:",correct_count, total_true_count, float(correct_count)/total_true_count
    print "False Positive:", float(total_exp_count-correct_count)/total_exp_count

def pipeline(f, svm):
    test_text = Text.Text(f.read())
    f.close()
    if(os.path.exists('/tmp/medications/')):
        if os.path.exists('/tmp/medications/output'):
            os.remove('/tmp/medications/output')
    else:
        os.mkdir('/tmp/medications/')

    result = test_text.getListRange(svm)

    meds = test_text.findMeds()
    #print "INFO:  Text analysis complete."

    #print [r[0] for r in result]
    q = test_text.cluster([r[0]for r in result], eps=4, minpoints=2)

    #print "Possible lists: ",q

    fout = open('/tmp/medications/output','w')
    for l in q:
        min_value = min(l)
        max_value = max(l)

        list_meds = list()
        for word, i in meds:
            if i in range(min_value,max_value):
                list_meds.append(word)


        k = test_text.getLines(r=(min_value, max_value))

        #print "+++++++++++++++++++++"
        new_list = List.List(min_value, max_value,raw_text="\n".join(k),medications=meds)
        for line in new_list.getLines():
            fout.write(line+'\n')
    fout.close()
    print "/tmp/medications/output closed"



if __name__ == "__main__":
    """
    tf_file = open("../../test_data_type_1/KEY","r")
    tf = [line.split(", ") for line in tf_file]
    tf = [[i.rstrip(), int(j.strip().rstrip()), int(k.strip().rstrip())] for i,j,k in tf]
    tf_file.close()
    svm = ListSVM.ListSVM("../../test_data_type_1/svm_train_master_2.data")
    print "INFO: SVM initalized and trained."

    result = testClustering(open("../../test_data_type_1/KEY","r"), svm)    

    fout = open("../resources/results/clustering_results_2013-06-27_10.csv",'w')
    
    for key, value in result.iteritems():
        s = str(key)+", "+str(value[1][0])+", "+str(value[1][1])+", "+str(value[0][0])+", "+str(value[0][1])+"\n"
        print s
        fout.write(s)
    fout.close()
    print "COMPLETE"
    analyzeClusterResults(open("../resources/results/clustering_results_2013-06-27_10.csv",'r'))
    """
    svm = ListSVM.ListSVM("/vagrant/Medications/resources/test_data_type_1/svm_train_master_2.data")
    #test_file = open("../../test_data_type_1/408381","r")
    #test_file = open("../../test_data_type_1/408901","r")
    #test_file = open("../../test_data_type_1/312287","r")
    #test_file = open("../../test_data_type_1/410131","r")
    #test_file = open("../../test_data_type_1/410688","r")  #NO LIST FOUND

    
    #test_file = open("../../test_data_type_1/410758","r")
    #test_file = open("../../test_data_type_1/423034","r")
    #test_file = open("../../test_data_type_1/427771","r")
    #test_file = open("../../test_data_type_1/428715","r")
    #test_file = open("../../test_data_type_1/432852","r")
    #test_file = open("../../test_data_type_1/436460","r")
    #test_file = open("../../test_data_type_1/439766","r")
    #test_file = open("../../test_data_type_1/442126","r")
    #test_file = open("../../test_data_type_1/444686","r")
    #test_file = open("../../test_data_type_1/444804","r")
    #test_file = open("../../test_data_type_1/448263","r")

    pipeline(open("/tmp/medications/input","r"), svm)
    """
    test_text = Text.Text(test_file.read())
    result = test_text.getListRange(svm)

    meds = test_text.findMeds()
    print "INFO:  Text analysis complete."

    print [r[0] for r in result]
    q = test_text.cluster([r[0]for r in result], eps=4, minpoints=2)

    print "Possible lists: ",q

    possible_lists = list()
    for l in q:
        min_value = min(l)
        max_value = max(l)

        list_meds = list()
        for word, i in meds:
            if i in range(min_value,max_value):
                list_meds.append(word)


        k = test_text.getLines(r=(min_value, max_value))

        print "+++++++++++++++++++++"
        new_list = List.List(min_value, max_value,raw_text="\n".join(k),medications=meds)

    """
    """
        possible_lists.append(new_list.parseList())

    current_max = -1
    current_max_i = -1
    i = 0
    for l in possible_lists:
        if len(l) >= current_max:
            current_max = len(l)
            current_max_i = i
        i += 1 
    #if current_max > 0: 
    final_list = possible_lists[current_max_i]
    for i in final_list:
        print i
    #else:  
        #print "NO LIST"
    """

    #print '===================='
    
    #print "COMPLETE"
