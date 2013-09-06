import math
import numpy as np
import matplotlib.pyplot as plt

class LogisticRegression:
    """
    A class for making binary decisions based on previously learned data.
    Use:
        - constructor:  an empty coefficent vector
        - Call self.train(x,y,alpha) to train the data where x is a 
            matrix of training data and y is the binary options (1,0)
        - Call evaluate with a particular set of data to determine the result.
    """
    def __init__(self, theta=[], print_flag=False):
        self.theta = theta
        self.print_flag = print_flag
    
    def eval(self,x):
        return self.evaluate(x)

    def evaluate(self,x):
        value = self.hypo(x)
        if value >= 0.50:
            return 1
        else:
            return 0

    def hypo(self, x_vector):
        if type(x_vector) is not list:
            x_vector = [x_vector]
        total = 1
        x_vector = [1] + x_vector
        if len(x_vector) != len(self.theta):
            raise ValueError("Length of input vector must be "+\
             str(len(self.theta)-1)+" to match the object's coefficent vector.")
        for x, t in zip(x_vector, self.theta):
            r = pow(math.e, float(x*t))
            total *= r
        value = float(1/(1+total)) 
        return value

    def cost(self, x,y):
        m = len(x)
        total = 0
        for i,j in zip(x,y):
            if type(i) is not list:
                i = [i]
            p = self.hypo(i)
            q = 1-self.hypo(i)
            total += j*math.log(p) + (1-j)*(math.log(q))
        value = (-1.0/m)*total
        return value  

    def derivate_cost(self, x,y,n):
        m = len(x)
        total = 0
        for i,j in zip(x,y):
            if type(i) is not list:
                i = [i]
            if n != 0:
                total += (self.hypo(i)-j)*i[n-1]
            else:
                total += (self.hypo(i)-j)*1
        value = -1.0/m * total
        return value

    def train(self, x,y, alpha, theta=None):
        prev_result = None
        if theta != None: self.theta = theta
        temp_theta = self.theta[:]
        result = 1
        while(result > alpha):
            count = 0
            for t in self.theta:
                p = self.derivate_cost(x,y,count) 
                temp_theta[count] -= alpha *p 
                count += 1
            self.theta = temp_theta[:]
            result = self.cost(x,y)
            if prev_result is not None and abs(prev_result-result)<=0.00000000001: break
            else: prev_result = result
            if self.print_flag: print "result: "+str(result)
        return result, self.theta, [self.hypo(i) for i in x]  
                
class Regression:
    def __init__(self, theta=[], print_flag=False):
        self.theta = theta
        self.print_flag = print_flag
    
    def hypo(self, x_vector):
        """
        preforms a basic linear regression by default, 
        extend and alter this method for 
        more complex polynomials.
        """
        if type(x_vector) is not list:
            x_vector = [x_vector]
        if len(x_vector) == len(self.theta)-1:
            #the x vector needs a 1 prepended to it to deal with the x0 case.
            x_vector = [1] + x_vector[:]
        else:
            raise ValueError(\
            "Length of input vector \
            must be "+str(len(self.theta)-1)+" to match \
            the object's coefficent vector."\
            )
        value = np.dot(x_vector, self.theta)     
        return value 

    def cost(self, x,y):
        """
        Computes a basic cost function with matrix multiplication 
        for a linear/polynomial regression. 
        """
        m = len(x)
        total = 0
        for i,j in zip(x,y):
            if type(i) is not list:
                i = [i]
            p = self.hypo(i)
            total += pow(p-j,2)
        value = (0.5/m)*total
        return value  

    def derivate_cost(self, x,y,n):
        m = len(x)
        total = 0
        for i,j in zip(x,y):
            if type(i) is not list:
                i = [i]
            if n != 0:
                total += (self.hypo(i)-j)*i[n-1]
            else:
                total += (self.hypo(i)-j)*1
        value = 1.0/m * total
        return value

    def train(self, x,y, alpha, theta=None):
        prev_result = None
        if theta != None: self.theta = theta
        temp_theta = self.theta[:]
        result = 1
        while(result > alpha):
            count = 0
            for t in self.theta:
                p = self.derivate_cost(x,y,count) 
                temp_theta[count] -= alpha *p 
                count += 1
            self.theta = temp_theta[:]
            result = self.cost(x,y)
            if prev_result is not None and abs(prev_result-result)<=0.00000000001: break
            else: prev_result = result
            if self.print_flag: print "result: "+str(result)
        return result, self.theta, [self.hypo(i) for i in x]  

class Plotter:
    """
    Series of tool for plotting common patterns
    """
    def __init__(self):
        pass
    def linear_fit(self, x,y,r):
        """
        Applies a basic linear fit based on coeffs in regression object to the 
        data x,y
        - plots the values of (i,j) for i,j in zip(x,y)
        - plots the projected/learned curve/fit according to regression object
        """
        x_min = min(x)-1
        x_max = max(x)+2
        y_min = min(y)-1
        y_max = max(y)+1
        x_range = range(x_min, x_max)
        plt.plot(x,y,"ro",x_range, [r.hypo(i) for i in x_range])
        plt.axis([x_min,x_max,y_min,y_max])
        plt.show()

    def binary_plot(self, x, y, z, r):
        x1 = list()
        y1 = list()

        x2 = list()
        y2 = list()
        if not z:
            z =  [r.evaluate([i,j])for i,j in zip(x,y)]
        for i,j,k in zip(x,y,z):
            if k == 1:
                x1.append(i)
                y1.append(j)
            else:
                x2.append(i)
                y2.append(j)
        plt.plot(x1,y1,"ro",x2,y2,"bo")
        plt.axis([min(x)-1, max(x)+1, min(y)-1, max(x)+1])
        plt.show()

class LinearRegression(Regression):
    def __init__(self, theta=[], print_flag=False):
        Regression.__init__(self,theta, print_flag)

if __name__ == "__main__":
    """
    r = LinearRegression([0,0], print_flag=True)
    r.train([1,2,3],[1,2,3],0.1)
    p = Plotter()
    p.linear_fit([1,2,3],[1,2,3],r)
    """
    r = LogisticRegression([0,0,0], print_flag=True)
    x = [1,1,1,2,3,3,3,4,5]
    y = [1,2,3,1,3,4,5,3,4]
    z = [1,1,1,1,1,0,0,0,0]
    r.train([[i,j] for i,j in zip(x,y)],z,0.1)

    p = Plotter()
    x2 = [0,0,1,2,2.4,2.5,4,5,5,6]
    y2 = [0,0.5,1,3,4,2,4.5,4,3,5]
    p.binary_plot(x2,y2,[],r)

