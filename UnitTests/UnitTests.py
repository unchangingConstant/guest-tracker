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
        self.cursor.execute("DELETE FROM students;")
        self.cursor.execute("DELETE FROM histories;")
        self.database.commit()

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
    
    def buttonedListViewFixture(self):
        self.model = QtCore.QStringListModel()
        self.list = ["boogers", "peepee", "poopoo"]
        self.model.setStringList(self.list)

        self.testWidget = Custom.ButtonedListView()
        self.testWidget.setModel(self.model)

    def startWidget(self, widget: QtWidgets.QWidget):
        if __name__ == "__main__":
            window = widget
            window.resize(800, 600)
            window.show()
            sys.exit(self.app.exec())

    def testButtonedListView1(self):   #   Tests that the widget will render button in lists
        self.testFixture()
        self.buttonedListViewFixture()

        self.startWidget(self.testWidget)
    
    def testButtonedListView2(self):   #    Tests that ButtonedListView will run set function upon press of button
        self.testFixture()
        self.buttonedListViewFixture()

        def testFunc(data):
            print(f"{data}!")
        
        self.testWidget.setButtonFunction(testFunc)

        self.startWidget(self.testWidget)
    
    def testButtonedListView3(self):    #   Tests that ButtonedListView's button function will have the specified data role passed to it
        self.testFixture()
        self.buttonedListViewFixture()

        def testFunc(data):
            print(f"{data}!")

        self.testWidget.setButtonFunction(testFunc)
        self.testWidget.setButtonFunctionPassedDataRole(QtCore.Qt.DisplayRole)

        self.startWidget(self.testWidget)

    """
    Test below still failing
    """
    def testButtonedListView4(self):   #   Tests that ButtonedListView will run function even if it takes no parameters
        self.testFixture()
        self.buttonedListViewFixture()

        def testFunc():
            print("Epic!")

        self.testWidget.setButtonFunction(testFunc)
        self.startWidget(self.testWidget)

    def testButtonedListView5(self):    #   Test for when you're able to set the button text
        self.testFixture()
        self.buttonedListViewFixture()
        
        self.testWidget.setButtonText("Luna")
        self.startWidget(self.testWidget)

    def testButtonedListView6(self):    #   Tests that function still executes on button press when listView is set to no editTriggers
        self.testFixture()
        self.buttonedListViewFixture()
        self.testWidget.setEditTriggers(QtWidgets.QAbstractItemView.NoEditTriggers)

        def testFunc(data):
            print("Epic!")

        self.testWidget.setButtonFunction(testFunc)
        self.startWidget(self.testWidget)

    def testDurationWidget(self):
        pass

uTest = UnitTest()
uTest.testButtonedListView6()