from PyQt5 import QtWidgets, QtCore, QtSql
import sqlite3
import os
import sys

current_dir = os.path.dirname(os.path.abspath(__file__))
parent_dir = os.path.dirname(current_dir)
sys.path.append(parent_dir)

import customWidgets as Custom

class UnitTest():

    def databaseFixture(self):
        self.app = QtWidgets.QApplication([])

        abspath = os.path.abspath(__file__) 
        dirname = os.path.dirname(abspath) 
        dirname = os.path.join(dirname, "TestData") 

        #Resets the databases
        self.database = sqlite3.connect(os.path.join(dirname, "testDB.db"))
        self.cursor = self.database.cursor()
        #self.cursor.execute("DELETE FROM students;")
        #self.cursor.execute("DELETE FROM histories;")
        #self.database.commit()

        #########################################################################

        self.studentDB = QtSql.QSqlDatabase().addDatabase("QSQLITE")  
        self.studentDB.setDatabaseName(os.path.join(dirname, "testDB.db"))  

        self.students = QtSql.QSqlTableModel()
        self.students.setTable("students")
        self.students.select()

        self.histories = QtSql.QSqlTableModel()
        self.histories.setTable("histories")
        self.histories.select()
    
    def testFixture(self):
        self.app = QtWidgets.QApplication([])

    def startWidget(self, widget: QtWidgets.QWidget):
        if __name__ == "__main__":
            window = widget
            window.resize(800, 600)
            window.show()
            sys.exit(self.app.exec())

    def testWidgetPerItemListView1(self):   #   Tests that the widget will simply start
        self.testFixture()

        model = QtCore.QStringListModel()
        list = ["boogers", "peepee", "poopoo"]
        model.setStringList(list)

        testWidget = Custom.WidgetPerItemListView()
        testWidget.setModel(model)

        self.startWidget(testWidget)
    
    def testWidgetPerItemListView2(self):   #   Tests that the widget will render widgets into list
        self.testFixture()

        model = QtCore.QStringListModel()
        list = ["boogers", "peepee", "poopoo"]
        model.setStringList(list)

        testWidget = Custom.WidgetPerItemListView()
        testWidget.setModel(model)

        button = QtWidgets.QPushButton
        testWidget.addWidget(button, 0)

        self.startWidget(testWidget)
    
    def testWidgetPerItemListView3(self):    #   If __updateWidgets is called but no model is set, program doesn't kill itself
        pass

    def testWidgetPerItemListView4(self):   #   Widgets are placed to the right of view data/items
        pass

uTest = UnitTest()
uTest.testWidgetPerItemListView2()