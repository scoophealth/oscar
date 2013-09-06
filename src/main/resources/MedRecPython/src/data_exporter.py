import Text
import Database

PATH = "../../test_data_type_1/"
SERIES = "./"
db = Database.Database("127.0.0.1","root","05sc@r","drugref")

def exportData(keys, fout):
    count = 0
    for f,s,e in keys:
        print "-------------",count, f,
        text = Text.Text(open("../../test_data_type_1/"+f,"r").read())
        meds = text.findMeds(db)
        print "med_id",

        mfd = text.getMedFreqDist(meds)
        print "mfd",
        line_dist = [k if k is not None else -1 for k in text.determineLineDistance(['MEDICATIONS',"DISCHARGE","INSTRUCTIONS"], percent=True)]
        print "line_dist",
        word_length_dist = text.determineWordLengthDist()
        print "word_length_dist",
        ac_dist = text.acronymFreq()
        print "ac_ist",
        noun_dist = text.determinePosFreq("NN")
        print "noun_dist",
        verb_dist = text.determinePosFreq("VB")
        print "verb_dist",
        art_dist = text.determinePosFreq("DT")
        print "art_dist",
        adj_dist = text.determinePosFreq("JJ")
        print "adj_dist",
        con_dist = text.determinePosFreq("CC")
        print "con_dist",
        print "."

        truth_vals = ["+"+str(1) if l in range(int(s),int(e)+1) else str(-1) for l in range(len(text.getLines()))]

        text_values = zip(line_dist, mfd, noun_dist, verb_dist, adj_dist, art_dist, con_dist, word_length_dist, ac_dist)

        for value, truth in zip(text_values, truth_vals):
            value = map(str, value)
            value = [str(i+1)+":"+value[i] for i in range(len(value))]
            print value
            fout.write(" ".join([truth]+value)+"\n")
        count += 1

def massageData(fin, fout, features, num=None):
    count = 0
    for line in fin:
        if num is not None and count > num:
            break
        new_line = list()
        line = line.split(" ")
        line = [l.strip().rstrip() for l in line]
        for feature,i in zip(line, range(len(line))):
            if i in features:
                if i == 1:
                    j,k = feature.split(":")
                    k = float(k)
                    if k<0.0: k = -1
                    feature = ":".join([j,str(k)])
                new_line.append(feature)
        fout.write(" ".join(new_line)+"\n")
        count += 1

def newTestSeries(test_min, test_max):
    #test_file = open(PATH+SERIES+'svm_test.data', "w")
    train_file = open(PATH+SERIES+'svm_100.data', "w")
    key_file = open(PATH+"KEY", "r")
    test_keys = list()
    train_keys = list()
    count = 1

    rmin = test_min
    rmax = test_max

    for line in key_file:
        f, s, e = line.split(", ")
        f = f.strip().rstrip()
        s = s.strip().rstrip()
        e = e.strip().rstrip()
        if count in range(rmin, rmax+1):
            test_keys.append((f,s,e))
        else:
            train_keys.append((f,s,e))
        count += 1

    exportData(train_keys, train_file)
    #exportData(test_keys, test_file)

    print "COMPLETED"
    #test_file.close()
    train_file.close()
    db.close()
    key_file.close()


if __name__ == "__main__":
    #0: REQUIRED, label
    #1: line_dist
    #2: mfd
    #3: noun_dist
    #4: verb_dist
    #5: adj_dist
    #6: art_dist
    #7: con_dist
    #8: word_length_dist
    #9: ac_dist
    massageData(open("../../test_data_type_1/svm_100.data", 'r'), open("../../test_data_type_1/svm_100_master.data","w"),[0,2,3,4,5,6,7,8,9])
    #massageData(open("../../test_data_type_1/svm_test_master.data", 'r'), open("../../test_data_type_1/series_11/svm_test.data","w"),[0,4,9])
        
    #newTestSeries(-1,-1)

    print "COMPLETE"
    print "Simon"

