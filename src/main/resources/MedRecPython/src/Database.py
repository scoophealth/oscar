import MySQLdb as mdb

class Database:
    def __init__(self, host, user, pwd, db, port=None):
        if port is not None:
            self.con = mdb.connect(host=host,
                             user=user,
                             passwd=pwd,
                             db=db,
                             port=port)
        else:
            self.con = mdb.connect(host=host,
                         user=user,
                         passwd=pwd,
                         db=db)

        self.cur = self.con.cursor()
        #print "INFO:  Initalized Database connection"

    def execute(self, string):
        try:
            self.cur.execute(string)
        except:
            return []
        return self.cur.fetchall()

    def search_drugref(self, name):
        """
        @param name str:  A single string to search
                          for in multiple tables of the DB.
        @return boolean: True if there are any hits, False otherwise.
        """
        sql_1 = "SELECT brand_name FROM cd_drug_product WHERE brand_name LIKE '%"+name+"%';"
        sql_2 = "SELECT tc_atc FROM cd_therapeutic_class WHERE tc_atc LIKE '%"+name+"%';"
        sql_3 = "SELECT ingredient FROM cd_active_ingredients WHERE ingredient LIKE '%"+name+"%';"
        sql_list = [sql_1, sql_2, sql_3]
        if [True for sql in sql_list if self.execute(sql)]:
            return True
        else:
            return False



    def close(self):
        self.con.close()

if __name__ == "__main__":
    db = Database("127.0.0.1","root","05sc@r","drugref")
    #print db.execute("select brand_name from cd_drug_product where brand_name like '% DOG %';")
    #print db.search_drugref("DOG")
    db.close()