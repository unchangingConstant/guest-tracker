import sys
import os
from PyQt5 import QtCore, QtWidgets, QtSql, Qt
import customWidgets as custom

abspath = os.path.abspath(__file__) #   Takes current program path
dirname = os.path.dirname(abspath)  #   Gets current folder where program is
dirname = os.path.join(dirname, "userdata") #   Creates path for the folder "userData"
os.chdir(dirname)   #   Sets cwd to the folder where the database it!

class TestApp(QtWidgets.QWidget):
    def __init__(self):
        super().__init__()

        self.layout = QtWidgets.QVBoxLayout()
        self.setLayout(self.layout) #   Sets testApp layout

        self.initModels()
        self.initAddVisitsWidget()
        self.initVisitsDisplay()

    def initAddVisitsWidget(self):  #   Read all the info you seek in customWidgets.AddVisitsWidget()
        self.addVisitsWidget = custom.AddVisitsWidget(self.studentModel, self.visitModel)
        self.layout.addLayout(self.addVisitsWidget)

    def initVisitsDisplay(self):
        self.visitsDisplay = custom.VisitsDisplay()
        self.layout.addWidget(self.visitsDisplay)

    def initModels(self):
        self.studentDB = QtSql.QSqlDatabase().addDatabase("QSQLITE")  #   Opens a sort of "tool" to access the database(?)
        self.studentDB.setDatabaseName("database.db")  #   Adds the database to be accessed

        self.studentModel = QtSql.QSqlTableModel() #   SqlTableModel is automatically connected to the database opened with the code above???
        self.studentModel.setTable("students") #   Sets model to table in the database we want to access
        self.studentModel.select() #   Selects all data on table to be displayed

        self.visitModel = QtSql.QSqlTableModel()
        self.visitModel.setTable("histories")
        self.visitModel.select()

    def initTableView(self):
        self.tvProxyModel = custom.CombineColumnsProxyModel([1, 2])   #   Creates the proxy model the search box will use
        self.tvProxyModel.setSourceModel(self.model)    #   Sets the proxy model's source model

        self.tableView = QtWidgets.QTableView() #   This widget will display the info in the database
        self.tableView.setModel(self.tvProxyModel) #   Sets the table's model to the database model (self.model in initModel())
        self.layout.addWidget(self.tableView)   #   Adds tableView to layout

    def initStudentNameView(self):
        self.firstNameView = QtWidgets.QListView()   #   Creates list to display first names
        self.firstNameView.setEditTriggers(QtWidgets.QAbstractItemView.EditTrigger.NoEditTriggers)  #   Makes the QListView uneditable
        self.firstNameView.setModel(self.model)  #   Sets the sql table model to the list widget
        self.firstNameView.setModelColumn(1) #   Sets column displayed to the first names of child

        self.listLayout.addWidget(self.firstNameView)   #   Adds firstNameView to layout

if __name__ == "__main__":
    app = QtWidgets.QApplication([])

    window = TestApp()
    window.resize(800, 600)
    window.show()

    sys.exit(app.exec())