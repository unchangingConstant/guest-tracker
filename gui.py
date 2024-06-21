import sys
import os
from PyQt5 import QtCore, QtWidgets, QtSql

abspath = os.path.abspath(__file__) #   Takes current program path
dirname = os.path.dirname(abspath)  #   Gets current folder where program is
dirname = os.path.join(dirname, "userdata") #   Creates path for the folder "userData"
os.chdir(dirname)   #   Sets cwd to the folder where the database it!

class TestApp(QtWidgets.QWidget):
    def __init__(self):
        super().__init__()

        self.initModel()    #   Sets up SqlTableModel
        self.initTableView()    #   Sets up table that displays Sql database

        self.layout = QtWidgets.QVBoxLayout()   #   Creates a layout
        self.layout.addWidget(self.tableView)   #   Adds searchBar to layout
        self.setLayout(self.layout)             #   Sets testApp layout

    def initModel(self):
        self.db = QtSql.QSqlDatabase().addDatabase("QSQLITE")  #   Opens this???
        self.db.setDatabaseName("database.db")  #   Adds the database to be accessed
        self.model = QtSql.QSqlTableModel() #   SqlTableModel is automatically connected to the database opened with the code above???
        self.model.setTable("students") #   Sets model to table in the database we want to access
        self.model.select() #   Selects all data on table to be displayed
    
    def initTableView(self):
        self.tableView = QtWidgets.QTableView() #   This widget will display the info in the database
        self.tableView.setModel(self.model) #   Sets the table's model to the database model (self.model in initModel())

if __name__ == "__main__":
    app = QtWidgets.QApplication([])

    window = TestApp()
    window.resize(800, 600)
    window.show()

    sys.exit(app.exec())