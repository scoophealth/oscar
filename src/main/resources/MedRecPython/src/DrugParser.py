import re
import pprint 
import pdb
import nltk
import sys

DEFAULT_GRAMMAR = """
DOSAGE: {<CD>}
UNIT: {<NNS>}
DOSAGE_CLAUSE:{((<TO>|<IN>)?<DOSAGE><UNIT>)}
NEGATIVE: {<RB>}
NP: {(<DOSAGE_CLAUSE><IN>?)?<PRP\$>?<DT>?<JJ>*(<NN>|<NNP>|<NNS>)+(<JJ>|<TO>)?<DOSAGE_CLAUSE>?}
VP: {<NEGATIVE>?<TO>?<VB.*>+}
ONP: {<NP>+(<IN>?<NP>*)*}
CLAUSE: {(<VP>*<TO>?<ONP>)|<ONP><VP>*}
"""
#NP: {(<CD>(<NNS>|<NN>)<IN>)?<PRP\$>?<DT>?<JJ>*(<NN>|<NNP>)+<DOSAGE_CLAUSE>?}

class DrugParser:
    def __init__(self, grammar=None, abbrev_file=None):
        """
        An object that can parse sentences and find the context 
        in which a particular drug name is used. 
        """

        self.pp = pprint.PrettyPrinter(indent=4)
        
        self.freq_abbrs = list()
        self.route_abbrs = list()
        self.time_abbrs = list()
        if abbrev_file is None: 
            #use default locations
            af = open("/vagrant/Medications/resources/keywords.csv","r")
            for line in af:
                line = line.split(", ")
                if line[0] == "ROUTE":
                    for i in range(1, len(line)):
                        self.route_abbrs.append(line[i])
                elif line[0] == "TIME":
                    for i in range(1, len(line)):
                        self.time_abbrs.append(line[i])
                elif line[0] == "FREQ":
                    for i in range(1, len(line)):
                        self.freq_abbrs.append(line[i])
                    
                        

        #choose the correct grammar
        if grammar is None:
            self.grammar = DEFAULT_GRAMMAR 
        else:
            self.grammar = grammar

        #create the NLTK regexp parser object.
        self.parser = nltk.RegexpParser(self.grammar)

        self.blank_td = {
                        "CLAUSE":None,
                        "ONP":None,
                        "NP":None,
                        "NOUN":None,
                        "DOSAGE_CLAUSE":None,
                        "DOSAGE":None,
                        "DOSAGE_AMOUNT":None,
                        "DOSAGE_UNIT":None,
                        "DOSAGE_FREQ":None,
                        "VP":None,
                        "VERB":None,
                        "NEGATIVE":None
                        }
        self.td = self.blank_td

    def generate_parse_tree(self, sent):
        """
        Generates a parse tree for the sentence sent based on the 
        self.grammar.

        @param sent (list[(word, pos)]): The incoming sentence, 
                    must be tokenized and tagged.  Can also take a str object
                    and will call tokenizing and pos_tagging on it.
        """ 
        if type(sent) is str:
            sent = nltk.pos_tag(nltk.word_tokenize(sent))
        sent = self.__detect_route(sent)
        sent = self.__detect_freq(sent)
        sent = self.__detect_time(sent)
        return self.parser.parse(sent) 
        
    def parse_sentence(self, sent, drugs):
        """
        Parses the sentence and identifies the drug(s), dosage and acting verb.
        - Generates a parse tree based on self.grammar.
        - Walks parse tree to find components of the sentence.
        - Returns a dict(s) of the drug, dosage, acting verb
        
        @param sent (list((word, pos))):  The tokenized and parsed
            version of the sentence. 
        @param drug list(str):  The drug name(s) to search for in the sentence
    
        @return list(dict{"drug":DRUG_NAME, "dosage":DOAGE, "verb":ACTING_VERB})
        """
        #re set the dictionary
        self.td = {
                        "CLAUSE":None,
                        "ONP":None,
                        "NP":None,
                        "NOUN":None,
                        "DOSAGE_CLAUSE":None,
                        "DOSAGE":None,
                        "UNIT":None,
                        "FREQ":None,
                        "TIME":None,
                        "ROUTE":None,
                        "VP":None,
                        "VERB":None,
                        "NEGATIVE":None
                        }

        if type(sent) is list or type(sent) is str:
            tree = self.generate_parse_tree(sent) 
        else:
            print "ERROR:  Expected sentence as a tokenized and tagged list() in DrugParser.parse_sentence()"
            exit()
        
        drug_info_list = list()
        for drug in drugs:
            #print drug
            #print tree
            drug_info = self.search_parse_tree(drug, tree)
            if not(drug_info is False or drug_info == []):
                drug_info_list.append(drug_info)
        return drug_info_list
            
    def __detect_time(self, sent):
        i = 0
        for word, pos in sent:
            if word[-1] == ".":
                word = word[:-1]
            if pos in ["NNP", "NN", "JJ"] and word.upper() in self.time_abbrs:
                self.td["TIME"] = word
                sent.pop(i)
                continue
            i += 1
        return sent
    def __detect_route(self, sent):
        i = 0
        #print sent
        for word, pos in sent:
            if word[-1] == ".":
                word = word[:-1]
            if word in self.route_abbrs:
                self.td["ROUTE"] = word
                sent.pop(i)
                continue
            i += 1
        return sent

    def __detect_freq(self, sent):
        i = 0
        for word, pos in sent:
            if word[-1] == ".":
                word = word[:-1]
            if word in self.freq_abbrs:
                self.td["FREQ"] = word
                sent.pop(i)
                continue
            i += 1
        return sent
        

    def search_parse_tree(self, drug_name, tree):
        """
        Searches the parse tree (tree) for the drug(drug_name) and returns
        the verb acting on the tree aswell as the dosage associated with the drug.  
        """
        try:
            noun_result = self.__find_drug_name(drug_name, tree, "S", 0)
        except Exception:
            return False 
        try:
            dosage_result = self.__find_dosage(tree)
        except Exception:
            return False 
        try:
            verb_result = self.__find_acting_verb(tree)
        except Exception:
            return False 
        if noun_result:
            drug_noun = tree[self.td["CLAUSE"]][self.td["ONP"]][self.td["NP"]][self.td["NOUN"]][0]
        else:
            drug_noun = None
        if dosage_result:
            drug_dosage = tree[self.td["CLAUSE"]][self.td["ONP"]][self.td["NP"]]\
                        [self.td["DOSAGE_CLAUSE"]][self.td["DOSAGE"]][0][0]
                        
            if self.td["UNIT"] is not None:
                try:
                    dosage_unit = tree[self.td["CLAUSE"]][self.td["ONP"]][self.td["NP"]][self.td["DOSAGE_CLAUSE"]][self.td["UNIT"]][0][0]
                except:
                    print "INFO:  EXCEPTION WAS CAUGHT AND HANDLED"
                    dosage_unit = None
            else:
                dosage_unit = None
        else:
            drug_dosage = None
            dosage_unit = None
        if verb_result:
            acting_verb = tree[self.td["CLAUSE"]][self.td["VP"]][self.td["VERB"]][0]
        else:
            acting_verb = None

        if self.td["NEGATIVE"] is not None:
            negative  = tree[self.td["CLAUSE"]][self.td["VP"]][self.td["NEGATIVE"]][0][0]
        else:
            negative = None

        #print self.pp.pprint(self.td)
        if drug_noun is not None:
            return {"drug_name":drug_noun, "verb":acting_verb, "dosage":drug_dosage, "unit":dosage_unit, "frequency":self.td["FREQ"], "route": self.td["ROUTE"], "negative":negative, "time":self.td["TIME"]}
        else: 
            return []
        
    def __find_acting_verb(self, tree):
        """
        Finds the acting verb on the ONP that contains the noun
        Pattern looks like:
            CLAUSE->VP->[(NEGATIVE->RB) and/or VERB]
        NOTE: This function is greedy, and will take the first VP in the 
        CLAUSE it sees as the acting verb phrase.
        """
        c_tree = tree[self.td["CLAUSE"]]
        i = 0
        for subtree in c_tree:
            if isinstance(subtree, nltk.tree.Tree):
                if subtree.node == "VP":
                    self.td["VP"] = i
            i += 1
        if self.td["VP"] is None:
            return False
        vp_tree = c_tree[self.td["VP"]]
        i = 0
        for subtree in vp_tree:
            if isinstance(subtree, nltk.tree.Tree):
                if subtree.node == "NEGATIVE":
                    self.td["NEGATIVE"] = i
            else:
                if subtree[1] in ["VB","VBN","VBG"]:
                    self.td["VERB"] = i
                    return True
            i += 1
        return False
        
                    
    def __find_dosage(self, tree):
        """
        Finds the dosage assoicated with the drug name.  Uses the information about 
        the location of drug dosage from self.td to determine 
        where to start looking.
        TODO:  Add in more complex parsing for complex dosage_clauses
        Looks for the following pattern:
            CLAUSE->ONP->NP->DOSAGE_CLAUSE->DOSAGE:CD and/or units 
            TODO: Make this pattern contain more than just CD and units, needs other items aswell.
        """
        #print tree
        np_tree = tree[self.td["CLAUSE"]][self.td["ONP"]][self.td["NP"]]
        i = 0
        for subtree in np_tree:
            if isinstance(subtree, nltk.tree.Tree):
                if subtree.node == "DOSAGE_CLAUSE":
                    self.td["DOSAGE_CLAUSE"] = i
                    break
            i += 1
        if self.td["DOSAGE_CLAUSE"] is None:
            return False
        dc_tree = np_tree[self.td["DOSAGE_CLAUSE"]]
        i = 0
        for subtree in dc_tree:
            if isinstance(subtree, nltk.tree.Tree):
                if subtree.node == "DOSAGE":
                    self.td["DOSAGE"] = i
                elif subtree.node == "UNIT":
                    self.td["UNIT"] = i
            i+=1
        #self.pp.pprint(self.td)
        if self.td["DOSAGE"] is not None:
            return True
        else:
            return False 
    def __find_drug_name(self, drug_name, tree, pos_type, position):
        """
        Searches for a noun that is the correct drug name.
        Stores the drug name path(node sequence) in the self.storage_dict
        The drug name will have a path of the following form:
         CLAUSE->OPN->NP->NN->DRUG_NAME
        """
    
        if pos_type == "NP":
            i = 0
            flag = False
            for subtree in tree:
                if isinstance(subtree,tuple):
                    tk = nltk.word_tokenize(drug_name)
                    for k in range(len(tk)):
                        if tree[i+k][0] == tk[k]:
                            flag = True
                            if k == 0: continue
                            try:
                                t = tree.pop(i+k)
                                string = str(tree[i][0])+"_"+str(t[0])
                                tree[i] = list(tree[i])
                                tree[i][0] = string
                                tree[i] = tuple(tree[i])
                            except Exception, err:
                                print err
                if flag:
                    self.td["NOUN"]=i
                    return True
                    
                i += 1 

        i = 0
        for subtree in tree:
            if pos_type == "S":
                if isinstance(subtree, nltk.tree.Tree) and subtree.node == "CLAUSE":
                    result = self.__find_drug_name(drug_name, subtree, "CLAUSE",i)
                    if result:
                        self.td["CLAUSE"] = i 
                        return True
            elif pos_type == "CLAUSE":
                if isinstance(subtree, nltk.tree.Tree) and subtree.node == "ONP":
                    result = self.__find_drug_name(drug_name, subtree, "ONP",i)
                    if result:
                        self.td["ONP"] = i 
                        return True
                    else:
                        i = i + 1
                        continue
            elif pos_type == "ONP":
                l = [w for w,p in subtree.leaves()]
                s = " ".join(l)
                #print subtree.leaves(), s,":",drug_name
                m = re.match(".*"+drug_name+".*",s)
                if m:
                    pass
                 #   print "match" 
                if isinstance(subtree, nltk.tree.Tree) and subtree.node == "NP":
                    result = self.__find_drug_name(drug_name, subtree, "NP",i)
                    if result:
                        self.td["NP"] = i 
                        return True
                    else:
                        i = i + 1
                        continue
            """
            elif pos_type == "NP":
                    if "NN" in subtree[1]:
                        if subtree[0] == drug_name:
                            self.td["NOUN"]=i
                            return True
                    elif subtree[1] == "JJ" and tree[i+1][0]==drug_name:
                        drug_tuple = tree[i+1]
                        new_name = subtree[0]+"_"+drug_tuple[0]
                        tree[i+1] = (new_name, "NN")
                        drug_name = new_name
            """
            i += 1 
        return False 
if __name__ == "__main__":
    drug_parser = DrugParser()
    if len(sys.argv) > 1:
        sent = sys.argv[1]
    else:
        print "Please enter a sentence to tag"
        print ">>>",
        sent = raw_input()
    result =  drug_parser.generate_parse_tree(sent)
    print result
    data = drug_parser.parse_sentence(sent, ["Aspirin", "Coreg"])
    print "================================================="
    print data
