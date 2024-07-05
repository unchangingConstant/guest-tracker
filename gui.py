import sys
import os
from PyQt5 import QtCore, QtWidgets, QtSql, Qt
import customWidgets as custom

abspath = os.path.abspath(__file__) #   Takes current program path
dirname = os.path.dirname(abspath)  #   Gets current folder where program is
dirname = os.path.join(dirname, "userdata") #   Creates path for the folder "userData"
#os.chdir(dirname)   #   Sets cwd to the folder where the database it!

class TestApp(QtWidgets.QWidget):
    def __init__(self):
        super().__init__()

        self.layout = QtWidgets.QVBoxLayout()
        self.setLayout(self.layout) #   Sets testApp layout

        self.initModels()
        self.initAddVisitsWidget()
        self.initVisitsDisplay()

    def initModels(self):
        self.studentDB = QtSql.QSqlDatabase().addDatabase("QSQLITE")  #   Opens a sort of "tool" to access the database(?)
        self.studentDB.setDatabaseName(os.path.join(dirname, "database.db"))  #   Adds the database to be accessed

        self.studentModel = QtSql.QSqlTableModel() #   SqlTableModel is automatically connected to the database opened with the code above???
        self.studentModel.setEditStrategy(QtSql.QSqlTableModel.OnFieldChange)
        self.studentModel.setTable("students") #   Sets model to table in the database we want to access
        self.studentModel.select() #   Selects all data on table to be displayed

        self.visitModel = QtSql.QSqlTableModel()
        self.visitModel.setEditStrategy(QtSql.QSqlTableModel.OnFieldChange)
        self.visitModel.setTable("histories")
        self.visitModel.select()
        self.visitModel.dataChanged.connect(lambda: self.visitingStudents.select())

        self.visitingStudents = QtSql.QSqlRelationalTableModel()
        self.visitingStudents.setEditStrategy(QtSql.QSqlTableModel.OnFieldChange)
        self.visitingStudents.setTable("histories")
        self.visitingStudents.setRelation(0, QtSql.QSqlRelation("students", "id", "student_name"))
        self.visitingStudents.setFilter("endTime IS NULL")
        self.visitingStudents.select()

        print(self.studentModel.editStrategy())
        print(self.visitModel.editStrategy())
        print(self.visitingStudents.editStrategy())

    def initAddVisitsWidget(self):  #   Read all the info you seek in customWidgets.AddVisitsWidget()
        self.addVisitsWidget = custom.AddVisitsWidget(self.studentModel, self.visitModel)
        self.layout.addWidget(self.addVisitsWidget)

    def initVisitsDisplay(self):
        self.visitsDisplay = custom.VisitsDisplay(self.visitingStudents)
        self.layout.addWidget(self.visitsDisplay)

if __name__ == "__main__":
    app = QtWidgets.QApplication([])

    window = TestApp()
    window.resize(800, 600)
    window.show()

    sys.exit(app.exec())