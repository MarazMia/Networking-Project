import datetime
import math
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import statistics as st

def cluster(y):
    clust=[max(y),min(y),st.mean(y)]
    print(clust)
    A=[]
    B=[]
    C=[]
    def _2(m,n):
        return abs(m-n)
    def min3(a,b,c):
        return min(min(a,b),c)
    for i in y:
        a=_2(clust[0],i)
        b=_2(clust[1],i)
        c=_2(clust[2],i)

        if a==min3(a,b,c):
            A.append(i)
            clust[0]=st.mean(A)
        elif b==min3(a,b,c):
            B.append(i)
            clust[1]=st.mean(B)
        else:
            C.append(i)
            clust[2]=st.mean(C)
    print(len(A),len(B),len(C))
    plt.scatter([1]*len(A),A)
    plt.scatter([2]*len(B),B)
    plt.scatter([3]*len(C),C)
    plt.show()
        
    
def meansquaredError(y,Y,n):
    err=0
    for i in range(n):
        err+=(y[i]-Y[i])**2
    return ((err)/n)**.5


def regression(x,y):
    if len(x)!=len(y):
        print('check your input')
        exit(0)

    n=len(x)
    avg_x=sum(x)/n
    avg_y=sum(y)/n

    x_deviations=[]
    y_deviations=[]
    for i in range(n):
        x_deviations.append(x[i]-avg_x)
        y_deviations.append(y[i]-avg_y)
    
    x_deviations_squared=[]
    for i in range(n):
        x_deviations_squared.append(x_deviations[i]**2)
    
    mult_of_XY_deviations=[]
    for i in range(n):
        mult_of_XY_deviations.append(x_deviations[i]*y_deviations[i])

    if sum(x_deviations_squared)>0:    
        B1=sum(mult_of_XY_deviations)/sum(x_deviations_squared)
        B0=avg_y-B1*avg_x
    else:
        B1=0
        B0=0

    '''table=pd.DataFrame({'x':x,'y':y,'x_dv':x_deviations,
              'y_dv':y_deviations,
             'mult_of_XY_dv':mult_of_XY_deviations,
             'x_dvsq':x_deviations_squared})
    print(table)
    print("B1 B0",B1,B0)'''

    newY=[]
    for i in range(n):
        newY.append(B1*x[i]+B0)
    plt.scatter(x,y)
    plt.plot(x,newY)
    plt.show()


def knn(x,y):
    if len(x)!=len(y):
        print('check your input')
        exit(0)

    n=len(x)
    dist=[i+1 for i in range(n-1)]
    predictions=[]
    for k in dist:
        p=[]
        for i in range(n):
            age=i-1
            pore=i+1
            total=0
            chk=0
            while chk!=k:
                if age>=0 and chk<k:
                    total+=y[age]
                    age-=1
                    chk+=1
                if pore<n and chk<k:
                    total+=y[pore]
                    pore+=1
                    chk+=1
            p.append(total/k)
        predictions.append(p)
    plt.plot(x,y,label="actual results",marker='o')
    err=10**2
    best=0
    for i in range(len(predictions)):
        if meansquaredError(y,predictions[i],len(y))<err:
            err=meansquaredError(y,predictions[i],len(y))
            best=i
    dv=[0]*len(y)
    for i in range(len(predictions)):
        dv[i]=y[i]-predictions[best][i]
        
    if len(predictions)>0:
        '''table=pd.DataFrame({'actual results':y,'best prediction':predictions[best],'deviation':dv})
        print(table,'\n')'''
        plt.plot(x,predictions[best],label="best prediction",marker='x')
        plt.legend()
        plt.show()


x = datetime.datetime.now()
x1 = x.year
x2 = x.month
lbl = ""
if (x2 == 1):
    file = open("jan21.txt","r")
    lbl = "jan21"
elif (x2 == 2):
    file = open("feb21.txt","r")
    lbl = "feb21"
elif (x2 == 3):
    file = open("mar21.txt","r")
    lbl = "mar21"
elif (x2 == 4):
    file = open("apr21.txt","r")
    lbl = "apr21"
elif (x2 == 5):
    file = open("may21.txt","r")
    lbl = "may21"
elif (x2 == 6):
    file = open("jun21.txt","r")
    lbl = "jun21"
elif (x2 == 7):
    file = open("jul21.txt","r")
    lbl = "jul21"
elif (x2 == 8):
    file = open("aug21.txt","r")
    lbl = "aug21"
elif (x2 == 9):
    file = open("sep21.txt","r")
    lbl = "sep21"
elif (x2 == 10):
    file = open("oct21.txt","r")
    lbl = "oct21"
elif (x2 == 11):
    file = open("nov21.txt","r")
    lbl = "nov21"
elif (x2 == 12):
    file = open("dec21.txt","r")
    lbl = "dec21"
d={}
income=0
Yincome=[]
price=[]
rani=[]
sundori=[]
for i in file:
    i=list(map(str,i.split()))
    rani.append(float(i[1]))
    sundori.append(float(i[2]))
    bal=i[3]
    price.append(float(bal))
    income+=float(bal)*(float(i[1])+float(i[2]))
    Yincome.append(float(bal)*(float(i[1])+float(i[2])))
    d[int(i[0])]=float(i[1])+float(i[2])
x=[]
y=[]
for i in d:
    x.append(i)
    y.append(d[i])
plt.bar(x,y,label = "rani " + str(round(st.mean(rani),2)) + "\nsundori " + str(round(st.mean(sundori),2)) + "\nprice freq " + str(st.mode(price)) + " " + str(price.count(st.mode(price))) + "\ntotal income "+str(income)+"\nPer day income "
        +str(round(income/len(d),2))+"\nmin milk "+str(min(y))+" liter max milk "
        +str(max(y))+" liter"+"\navg milk "+str(round(sum(y)/len(y),2))+" liter"+"\nin "+str(len(x))+" days"+"\ntotal milk "+str(sum(y))+" liter",color="red")
plt.legend()
plt.show()
plt.plot(x,Yincome,marker='o',label = "avg price " + str(round(st.mean(price),2)) + "\ntotal income "+str(income)+"\nPer day income "
        +str(round(income/len(d),2))+"\nin "+str(len(x))+" days"+"\ntotal milk "+str(sum(y))+" liter",color="green")
plt.legend()
plt.show()
knn(x,y)
regression(x,y)
cluster(y)
file.close()
