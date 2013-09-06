#! /usr/bin/python

__author__="sdiemert"
__date__ ="$Jun 17, 2013 4:11:39 PM$"

class DBScan:
    def __init__(self, eps, minpoints, data):
        self.eps = eps
        self.minpoints = minpoints
        self.data = [Point(value, i) for value,i in zip(data,range(len(data)))]

    def findClusters(self):
        cluster_id = 0
        for d in self.data:
            if d.getValue() == 0: 
                continue
            elif d.getCluster() == None:
                cluster_id += 1
            que = list()
            d.setTouched(True)
            n = self.findNeighborhood(d)
            if len(n) < self.minpoints:
                d.setCluster(-1)
                for x in n: x.setCluster(-1)
            else:
                for x in n:
                    x.setCluster(cluster_id)
                    if not x.getTouched():
                        que.append(x)
                while len(que) > 0:
                    p = que.pop(0)
                    p.setTouched(True)
                    r = self.findNeighborhood(p)
                    if len(r) >= self.minpoints:
                        for x in r:
                            x.setCluster(cluster_id)
                            if not x.getTouched():
                                que.append(x)
        return [d.getCluster() for d in self.data]


    def findNeighborhood(self, p):
        """
        Find the points in the neighborhood for point p.
        """
        if p.getPosition() - self.eps < 0:
            lower = 0
        else: lower = p.getPosition() - self.eps
        if p.getPosition() + self.eps >= len(self.data):
            upper = len(self.data)
        else: upper = p.getPosition() + self.eps

        r =  [k for k in self.data if (k.getPosition() in range(lower,upper+1)) and k.getValue()>=1]
        return r

    def findClusterRanges(self, dist):
        #print "HERE", dist
        return_list = list()
        curr_cluster = 0
        i = 0
        cluster_list = list()
        for line in dist:
            if line < 0 or line == None:
                pass
            elif line == curr_cluster:
                cluster_list.append(i)
            elif line != curr_cluster:
                return_list.append(cluster_list)
                cluster_list = [i]
                curr_cluster = line
            if i == len(dist) -1:
                #the last item in the list, to catch end clusters.
                return_list.append(cluster_list)
            i += 1
        return_list = [range(min(r)-5, max(r)+6) for r in return_list if len(r)>0] 
        #print "RETURN_LIST:", return_list
        print return_list
        return return_list

    def analyzeCluster(self, dist):
        """
        Analyzes the clusters to find occurances of keywords within them that
        would indicate they are part of a particular list (d/c med list)
        """
        ranges = self.findClusterRanges(dist)
        return ranges


class Point:
    def __init__(self, value, position):
        self.value = value
        self.position = position
        self.cluster = None
        self.touched = False

    def getValue(self):
        return self.value
    def getPosition(self):
        return self.position
    def getTouched(self):
        return self.touched
    def getCluster(self):
        return self.cluster
    def setTouched(self, value=True):
        self.touched = value
    def setCluster(self, value):
        self.cluster = value

if __name__ == "__main__":
    l = [1,1,1,1,1,0,0,0,0,0,0,0,1,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,1]
    l2 = [1,1,0,1,0,1,1,0,1,0,0,0,1,0,0,0,1,0,1,1,1,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,1]
    print l
    dbs = DBScan(3,2,l)
    r = dbs.findClusters()
    for a,b,c in  zip(range(len(l)),l,r):
        print a,b,c
    a = dbs.findClusterRanges(r)
    print a
