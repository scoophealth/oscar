from DrugFinder import DrugFinder
from MedFreqDist import MedFreqDist
from Database import Database
from Text import Text
from List import List
from ListFinder import ListFinder
import pprint


def getTruthData(filename, path_to_file, values):
    f = open(path_to_file+filename, 'r')
    data = list()
    if values is None:
        for line in f:
            line = line.split(",")
            line = [l.strip().rstrip() for l in line]
            data.append((line[0], int(line[1]), int(line[2])))

        return data
    else:
        for line in f:
            line = line.split(",")
            line = [l.strip().rstrip() for l in line]
            if line[0] in values:
                data.append((line[0], int(line[1]), int(line[2])))
        return data


def test_range(truth, exp):
    inside_count = 0
    outside_count = 0
    for i in exp:
        if i in truth:
            inside_count += 1
        else:
            outside_count += 1
    r = float(inside_count)/len(truth)
    if r == 1.0 and outside_count == 0:
        return r
    elif r == 0.0:
        return r
    elif r < 1.0:
        return r
    elif r == 1.0 and outside_count > 0:
        return float(inside_count + outside_count)/len(truth)

def test_file(filename, start, end, path):
    print filename+"----------------------"

    text = open(path+filename, 'r').read()
    text = Text(text)

    df = DrugFinder(text, Database("127.0.0.1","root","05sc@r","drugref"))
    nouns = df.findDrugs()
    print nouns

    lf = ListFinder()
    dc_lists = lf.identify_lists(text, nouns)
    print dc_lists
    dc_med_lists = list()
    if start == -1 and end == -1 and not dc_lists:
        return [1.0]

    for l in dc_lists:
        p = List()
        p.extract_list_from_text(l[0], l[1]+1, text)
        dc_med_lists.append(p)
        #print p.getRawText()

    r = list()
    for p in dc_med_lists:
        r.append(test_range(range(start,end+1), p.getRange(list=True)))

    if r == []:
        return [0.0]
    else:
        return r



def test(key_file, path_to_file, values=None):
    truth_data = getTruthData(key_file, path_to_file, values)
    result = list()
    for x,y,z in truth_data:
        r = test_file(x,y,z, path_to_file)
        print r
        result += r
    print result

if __name__ == "__main__":
    print "INFO:  STARTING"
    dc_med_lists = list()
    pp = pprint.PrettyPrinter(indent=4)
    test("KEY", "../../test_data_type_1/",values=["367805"])
    
    print "INFO: COMPLETED"
    exit()
    text = open('../../test_data_type_1/312287', "r").read()
    #text = open('../resources/test_data/163611', "r").read()
    #text = open('../../test_data_type_1/209730', "r").read()
    #text = open('../../test_data_type_1/207377', "r").read()
    #text = open('../../test_data_type_1/205199', "r").read()
    #text = open('../../test_data_type_1/203370', "r").read()
    text = Text(text)
    df = DrugFinder(text, Database("127.0.0.1","root","05sc@r","drugref"))
    nouns = df.findDrugs()
    #print nouns
    #mfd = MedFreqDist(text, nouns)
    #l = mfd.percentileMedDist()
    #print l
    #print "--------"
    #for x, c in zip(l, range(len(l))):
    #    print x, text.getLines()[c]
    #print"INFO:  COMPLETED"

    lf = ListFinder()
    dc_lists = lf.identify_lists(text, nouns)

    for l in dc_lists:
        p = List()
        p.extract_list_from_text(l[0], l[1]+1, text)
        dc_med_lists.append(p)

    for l in dc_med_lists:
        print l.getRawText()



    print "INFO:  END OF PROGRAM"

    