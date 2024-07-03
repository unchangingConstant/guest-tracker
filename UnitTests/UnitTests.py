from PyQt5 import QtWidgets, QtCore, QtSql
import sqlite3
import os
import sys

current_dir = os.path.dirname(os.path.abspath(__file__))
parent_dir = os.path.dirname(current_dir)
sys.path.append(parent_dir)

import customWidgets as Custom

class TestFixtureModel(QtSql.QSqlQueryModel):
    pass

class UnitTest():

    def testFixture1(self):
        self.app = QtWidgets.QApplication([])

        abspath = os.path.abspath(__file__) 
        dirname = os.path.dirname(abspath) 
        dirname = os.path.join(dirname, "TestData") 

        #Resets the databases
        self.database = sqlite3.connect(os.path.join(dirname, "testDB.db"))
        self.cursor = self.database.cursor()
        self.cursor.execute("DELETE FROM students;")
        self.cursor.execute("DELETE FROM histories;")
        self.database.commit()

        #########################################################################

        self.studentDB = QtSql.QSqlDatabase().addDatabase("QSQLITE")  
        self.studentDB.setDatabaseName(os.path.join(dirname, "testDB.db"))  

        self.students = QtSql.QSqlTableModel()
        self.students.setTable("students")
        self.students.select()

        self.histories = QtSql.QSqlTableModel()
        self.histories.setTable("histories")
        self.histories.select()

    def testDoesModelSyncOnSelect(self):
        self.testFixture1()
        addVisitsWidget = Custom.AddVisitsWidget(self.students, self.histories)
        addVisitsWidget.show()
        sys.exit(self.app.exec())
        

uTest = UnitTest()
uTest.testDoesModelSyncOnSelect()