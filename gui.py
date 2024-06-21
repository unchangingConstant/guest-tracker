import sys
import os
from PyQt5 import QtCore, QtWidgets, QtSql

abspath = os.path.abspath(__file__)
dirname = os.path.dirname(abspath)
dirname = os.path.join(dirname, "userdata")
os.chdir(dirname)

class TestApp(QtWidgets.QWidget):
    def __init__(self):
        super().__init__()

        self.tableView = QtWidgets.QTableView() #This widget will display the info in the database

        self.layout = QtWidgets.QVBoxLayout()   #Creates a layout
        self.layout.addWidget(self.tableView)   #Adds searchBar to layout
        self.setLayout(self.layout)             #Sets testApp layout

    def initModel(self):
        self.db = QtSql.QSqlDatabase.addDatabase("SQSQLITE")
        self.db.addDatabase("database.db")

if __name__ == "__main__":
    app = QtWidgets.QApplication([])

    window = TestApp()
    window.resize(800, 600)
    window.show()

    sys.exit(app.exec())