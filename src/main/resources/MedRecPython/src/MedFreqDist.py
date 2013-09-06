#! /usr/bin/python
import re
__author__="sdiemert"
__date__ ="$Jun 3, 2013 11:58:23 AM$"
class MedFreqDist:
    def __init__(self, text, meds):
         self.text = text
         self.meds = meds
         self.raw_dist = None
         self.dist = None

    def rawMedDist(self):
        """
        sets and returns the raw distribution of medications found.
        each line is assigned a value based on the number of medication hits.
        """
        dist = list()
        for line in self.text.getLines():
            line_count = 0
            for med, num in self.meds:
                if re.match(".*"+med.upper()+".*", line.upper()):
                    line_count += 1
            dist.append(line_count)
        self.raw_dist = dist
        return self.raw_dist

    def linePercentage(self, irange):
        if self.raw_dist is None:
            self.rawMedDist()
        if type(irange) is list or type(irange) is tuple:
            if len(irange) > 2:
                raise ValueError("Input to linePercentage() must be list of length 2 or int")
            else:
                return float(sum(self.raw_dist[irange[0]:irange[1]]))/sum(self.raw_dist)
        else:
            return float(self.raw_dist[irange])/len(self.raw_dist)

    def percentileMedDist(self):
        if self.raw_dist is None:
            self.rawMedDist()
        return_list = [float(value)/len(self.meds) if len(self.meds) != 0 else 0 for value in self.raw_dist ]
        self.dist = return_list
        return return_list

    def compile_ranges(self, ranges):
        prev_s = None
        prev_e = None

        i = 0
        while(True):
            if i >= len(ranges):
                break
            s,e = ranges[i]
            if i > 0:
                if prev_e is not None and abs(s-prev_e) <= 3:
                    ranges[i-1][1] = e
                    ranges.pop(i)
                    s = prev_s
                else:
                    i += 1
            else:
                i += 1
                prev_s = s
                prev_e = e
        return [[s,e] for s,e  in ranges if abs(e-s) >= 2]

    def find_ranges(self, dist=None):
        if dist is not None: self.dist = dist
        return_list = list()
        count = 0
        flag = False
        start = None
        end = None
        for value in self.dist:
            #print count, self.raw_dist[count], self.text.getLines()[count],
            if value > 0.0 and not flag:
                #print "IF 1"
                start = count
                flag = True
            elif value > 0.0 and flag:
                #print "IF 2"
                pass
            elif value <= 0.0 and flag:
                #print "IF 3"
                end = count - 1
                return_list.append([start,end])
                flag = False
            else:
                pass
                #print "IF 4"
            count += 1
        return return_list


if __name__ == "__main__":
    print "Hello World";
