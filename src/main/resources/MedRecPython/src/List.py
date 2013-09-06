#! /usr/bin/python

import re
from Text import Text
import DrugParser
__author__="sdiemert"
__date__ ="$Jun 3, 2013 11:04:25 AM$"

class List(Text):
    def __init__(self, start=None, end=None, medications=list(), raw_text=str()):
        if raw_text != str():
            if not medications:
                super(List, self).__init__(raw_text)
            else:
                super(List, self).__init__(raw_text, medications=medications)
        self.start = start  #the line that hte list starts on.
        self.end = end #the line that the list ends on.
        self.dp = DrugParser.DrugParser()
        self.raw_text = raw_text
        self.medications = medications

    def list_to_raw_text(self, list):
        return "\n".join(list)

    def getRawText(self):
        return self.raw_text
    def getRange(self, list=False):
        if list:
            return range(self.start, self.end+1)
        else:
            return (self.start, self.end)


    def extract_list_from_text(self, start, end, full_text):
        """
        - Sets and returns the text inside of a list that is
          designated to start at lines given by @param(start)
          and end at @param(end) from inside of full_text.
        - Line indexing starts from 0.

        """
        if self.start is None and self.end is None:
            self.start = start
            self.end = end
        count = 0
        for line in full_text.getLines():
            if count >= self.start and count <= self.end:
                self.text_list.append(line)
            count += 1
        self.text = self.list_to_raw_text(self.text_list)
        return self.text

    def standardize_units(self, sent):
        """
        Changes various unit types into their full name (mg -> milligrams)  
        Returns the sentence (str) with the units replaced.

        @param sent (str): The str version of the sentence.

        @return (str);  The sentence with units changed.
        """

        sent =  re.sub('mg', 'milligrams', sent, flags=re.IGNORECASE)
        sent =  re.sub('mcg', 'micrograms', sent, flags=re.IGNORECASE)
        sent =  re.sub(' g ', ' grams ', sent, flags=re.IGNORECASE)
        sent =  re.sub('mL', 'millilitres', sent, flags=re.IGNORECASE)
        sent =  re.sub('tab', 'tablets', sent, flags=re.IGNORECASE)
        sent =  re.sub('meq', 'milliequivalent', sent, flags=re.IGNORECASE)
        sent =  re.sub('cap', 'capsules', sent, flags=re.IGNORECASE)
        sent =  re.sub('capsule', 'capsules', sent, flags=re.IGNORECASE)
        return sent

    def parseList(self):
        return_list = list()
        for line, count in zip(self.getLines(), range(self.start,self.end+1)):
            d_list = list()
            for d,l in self.medications:
                if l == count:
                    d_list.append(d)
            line = self.standardize_units(line)
            return_list += self.dp.parse_sentence(line,d_list)
        return return_list
	
   

if __name__ == "__main__":
    print "Hello World";
