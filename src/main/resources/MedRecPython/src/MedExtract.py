# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.
from org.oscarehr.medextract import MedicationExtractorInterface;
#import Text
#import ListSVM
#import List
import imp


__author__="sdiemert"
__date__ ="$Jul 18, 2013 11:08:12 AM$"

class MedExtract(MedicationExtractorInterface):
    def __init__(self):
        ListSVM = imp.load_source('ListSVM', '/vagrant/Medications/src/ListSVM.py')
        self.svm = ListSVM("../../test_data_type_1/svm_train_master_2.data")

    def getMedList(self, text):
        Text = imp.load_source("Text","/vagrant/Medications/src/Text.py")
        self.text = Text(text)
        result = self.text.getListRange(self.svm)
        meds = self.text.findMeds()
        print [r[0] for r in result]
        q = self.text.cluster([r[0]for r in result], eps=4, minpoints=2)
        for l in q:
            min_value = min(l)
            max_value = max(l)

            list_meds = list()
            for word, i in meds:
                if i in range(min_value,max_value):
                    list_meds.append(word)

            k = test_text.getLines(r=(min_value, max_value))

            List = imp.load_source('List','/vagrant/Medications/src/List.py')
            new_list = List.List(min_value, max_value,raw_text="\n".join(k),medications=meds)
            return_str = ""
            for line in new_list.getLines():
                return_str += line+"\n"
            return return_str
        
