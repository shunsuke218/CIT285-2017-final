#!/usr/local/bin/jython
# -*- coding: utf-8 -*-
# Written by Shunsuke Haga
# Date: 2017/04/12
#
# Written for Python 2.7
# This script download Chicago marathon race result(2016) and store it as a mdb format.

import urllib, urllib2
import os
try:
    from bs4 import BeautifulSoup
except:
    print "Need to install library, BeautifulSoup to scrape page!"
    exit()
import time
import sys
import re
reload(sys)  
sys.setdefaultencoding('utf8')

try:
    from com.ziclix.python.sql import zxJDBC
except:
    print "Run in jython!"
    exit()

class Runner:
    def __init__(self, listinfo = [], *args):
        self.gender = ""
        self.plc_all = listinfo[0] if listinfo[0] is not None else ""
        self.plc_gen  = listinfo[1] if listinfo[1] is not None else ""
        self.plc_div  = listinfo[2] if listinfo[2] is not None else ""
        namecountry = re.findall("(.+?)(?:\([A-Z]+\))$", listinfo[3])
        self.name = namecountry[0] if len(namecountry) > 0 else listinfo[3]
        self.first = self.name.split(",")[0]
        self.last = self.name.split(",")[1].replace(" ", "")
        country = re.findall("\(([A-Z]+)\)$", listinfo[3])
        self.country = country[0] if len(country) > 0 else ""
        self.loc  = "\"" + listinfo[4] + "\"" if listinfo[4] is not None else ""
        self.bib  = listinfo[5] if listinfo[5] is not None else ""
        self.div  = listinfo[6] if listinfo[6] is not None else ""
        self.age  = listinfo[7] if listinfo[7] is not None else ""
        self.half  = listinfo[8] if listinfo[8] is not None else ""
        self.finish  = listinfo[9] if listinfo[9] is not None else ""

    def __str__(self):
        return "[ Gender: " + self.gender \
            + ", Place Overall: " + self.plc_all \
            + ", Place Gender: " + self.plc_gen \
            + ", Place Division: " + self.plc_div \
            + ", Name: " + self.name \
            + ", Country: " + self.country \
            + ", Location: " + self.loc \
            + ", Bib: " + self.bib \
            + ", Division: " + self.div \
            + ", Age: " + self.age \
            + ", Half: " + self.half \
            + ", Finish: " + self.finish + "]"

    def set_gender(self, gender):
        self.gender = gender
    def result (self):
        return [self.gender, self.plc_all, self.plc_gen , self.plc_div , self.first, self.last, self.country, self.loc , self.bib , self.div , self.age , self.half , self.finish ]



    
def main():

    with open("test.html") as html, open("output.csv", 'w') as output:
        output.write("Gender, Place Overall, Place Gender, Place Division, Last Name, First Name, Country, Location, Bib, Division, Age, Half, Finish\n")

        for gender in ["M", "W"]:
            for pagenum in range(1,5): # Setting it to 5 pages
                print "Operating page:" + str(pagenum) + " for gender:" + gender
                url = "http://results.chicagomarathon.com/2016/?page=" + str(pagenum) + "&event=MAR&lang=EN_CAP&num_results=1000&pid=list&search%5Bsex%5D=" + gender
                html = urllib2.urlopen(url)
                soup = BeautifulSoup(html.read())
                for index, tr in enumerate(soup.find_all("tr")[1:]):
                    runner = Runner([ td.string if index is not 3 else td.find("a").string \
                                   for index, td in enumerate(tr.find_all("td")) ])
                    runner.set_gender(gender)
                    #print runner.result()
                    output.write(','.join(runner.result()) + "\n")
            time.sleep(3)


    # set up constants
    currentdir = os.path.dirname(os.path.abspath(__file__))
    #DRV = 'net.ucanaccess.jdbc.UcanloadDriver'; MDB =  "//" + currentdir + "/output.mdb"
    MDB =  "jdbc:ucanaccess://" + currentdir + "/output.mdb"; DRV = "net.ucanaccess.jdbc.UcanloadDriver"
    #jdbc_url, username, password, driver_class  = "jdbc:ucanaccess:" + MDB, "", "", "net.ucanaccess.jdbc.UcanloadDriver"

    #cnxn = zxJDBC.connect(MDB, None, None, DRV)
    #cnxn = zxJDBC.connect(jdbc_url, username, password, driver_class)
    cnxn = zxJDBC.connect(MDB, "", "", driver_class)
    crsr = cnxn.cursor()
    print crsr.tables(None, None, '%', ('TABLE',))
    print crsr.fetchall()

    #crsr.execute("SELECT * FROM Amber ORDER BY Country")
    #print crsr.schema("Amber")
    crsr.execute("SELECT * FROM Amber ORDER BY G")
    print crsr.description
    for row in crsr.fetchall():
        print row[0], row[1], row[2], row[3], row[4], row[5], row[6]

    crsr.close()
    cnxn.close()


if __name__ == '__main__':
    main()

