#! /usr/bin/python

__author__="sdiemert"
__date__ ="$Jun 11, 2013 10:09:56 AM$"

class Medication:
    def __init__(self, brand_name=None, ingredient_names=list(), strength=None, unit=None, freq=None, route=None):
        self._brand_name = brand_name
        self._ingredient_names = ingredient_names
        self._strength = strength
        self._unit = unit
        self._freq = freq
        self._route = route

    def getMedicationString(self, brand_name=True):
        if brand_name:
            s = self._brand_name+" "
            s += str(self._strength)+" "
            s += self._unit+" "
            s += self._route+" "
            s += self._freq+" "
        else:
            s = str()
            for st in self._ingredient_names:
                s+=st+" "
            s += str(self._strength)+" "
            s += self._unit+" "
            s += self._route+" "
            s += self._freq+" "
        return s

    def getBrandName(self):
        return self._brand_name
    def getIngredientNames(self):
        return self._ingredient_names
    def getStrength(self):
        return self._strength
    def getUnit(self):
        return self._unit
    def getFreq(self):
        return self._freq
    def getRoute(self):
        return self._route

    def setBrandName(self, name=str()):
        self._brand_name = name
    def setIngredientNames(self, names=list()):
        self._ingredient_names = names
    def setStrength(self, i=None):
        self._strength = i
    def setUnit(self, unit=None):
        self._unit = unit
    def setFreq(self, f):
        self._freq = f
    def setRoute(self, r):
        self._route = r

    def __str__(self):
        return self.getMedicationString(brand_name=True)
    def __repr__(self):
        return self.getMedicationString(brand_name=True)


if __name__ == "__main__":
    m = Medication(brand_name="Aspirin",strength=50, unit="mg", freq="BID", route="PO")
