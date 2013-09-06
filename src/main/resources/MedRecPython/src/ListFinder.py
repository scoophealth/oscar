#! /usr/bin/python

__author__="sdiemert"
__date__ ="$Jun 3, 2013 3:27:13 PM$"

import re
from MedFreqDist import MedFreqDist as MFD
from MachineLearning import LogisticRegression as lr

class ListFinder:
    def __init__(self):
        self.bullet_patterns = ["^[0-9]{1,2}\.\s{1,2}(.*)$","^\-\s{,2}(.*)$"]
        self.regression = lr()
        self.keywords = ["DISCHARGE MEDICATIONS", "DISCHARGE MEDS", "D/C MEDICATIONS","DISCHARGE","DISCHARGED"]

    def match_bullets(self, text):
        """
        Matches each line for a possible bullet at the beginning of the line.
        Returns ranges of lines that are bullet points.
        """
        start = None
        end = None
        count = 0
        return_list = list()
        for line in text.getLines():
            if not start and [True for pattern in self.bullet_patterns if re.match(pattern, line)]:
                start = count
            elif start and [True for pattern in self.bullet_patterns if re.match(pattern, line)]:
                pass
            elif start:
                end = count - 1
                return_list.append([start, end])
                start = None
                end = None
            count += 1
        return return_list

    def combine_lists(self, line_ranges):
        """
        Combine lists are are within 3 lines of each other, because it is
        likely that they are in the same list, just with a few lines inbetween.
        """
        i = 0
        prev_e = None
        prev_s = None
        while(True):
            if i >= len(line_ranges):
                break
            s,e = line_ranges[i]
            if i != 0:
                if prev_e is not None and abs(s - prev_e) <=3:
                    #we have found 2 seperate lists that are likely together.
                    line_ranges[i-1][1] = e
                    line_ranges.pop(i)
                    s = prev_s
                else:
                    #did not find a similar range, store prev start and finish for later.
                    i += 1
            else:
                i += 1
            prev_e = e
            prev_s = s
        #return [[s,e] for s,e  in line_ranges if abs(e-s) >= 1]
        return [[s,e] for s,e  in line_ranges]


    def identify_lists(self, text, nouns):
        """
        Finds a list of medications (identified by nouns) in the text object (text)

        - uses an analysis of the bullets for the list if they exist.
        """
        mfd = MFD(text, nouns)
        dist = mfd.rawMedDist()
        #print zip(dist, range(len(dist)))
        dist_ranges = mfd.find_ranges(dist)
        dist_ranges = self.combine_lists(dist_ranges)

        #print "dist-ranges: ",dist_ranges


        bullet_ranges = self.match_bullets(text)
        bullet_ranges = self.combine_lists(bullet_ranges)

        #print "bullet-ranges:",bullet_ranges


        ranges = self.compare_ranges(bullet_ranges+dist_ranges)

        print ranges

        return [irange for irange in ranges if self.determineListTitle(text, irange)]

    def compare_ranges(self, ranges):
        return_list = list()
        i = 0
        while len(ranges) > 0:
            append_flag = False
            irange = ranges.pop(i)
            remove_list = list()
            #print irange,
            if len(ranges) == 0:
                return_list.append(irange)
            else:
                for jrange in ranges:
                    if irange[0] in range(jrange[0]-5,jrange[1]+5) and irange[1] in range(jrange[0]-5,jrange[1]+5):
                        #TODO:  FIXME
                        #print "there"+str(jrange),
                        return_list.append(jrange)
                        remove_list.append(jrange)
                        append_flag = True
                    elif jrange[0] in range(irange[0]-5,irange[1]+5) and jrange[1] in range(irange[0]-5,jrange[1]+5):
                        #print "here",
                        return_list.append(irange)
                        append_flag = True

                #print append_flag
                if not append_flag:
                    return_list.append(irange)
                else:
                    append_flag = False
            

            for k in remove_list:
                ranges.remove(k)
        return return_list

    def determineListTitle(self, text, irange):
        for line in text.getLines()[irange[0]-2:irange[1]+1]:
            if re.match(".*DISCHARGE.*", line.upper()) and re.match(".*(MEDICATION|INSTRUCTIONS).*", line.upper()):
                return True
        return False

if __name__ == "__main__":
    print "Hello World";
