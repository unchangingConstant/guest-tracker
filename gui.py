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

        self.layout = QtWidgets.QVBoxLayout()   #   Creates a layout
        self.listLayout = QtWidgets.QHBoxLayout()   #   Creates widget for two lists to display things at a time
        self.layout.addLayout(self.listLayout)  #   Adds listLayout to central layout
        self.setLayout(self.layout)             #   Sets testApp layout

        self.initModel()
        self.initSearchBox()

    def initModel(self):
        self.db = QtSql.QSqlDatabase().addDatabase("QSQLITE")  #   Opens a sort of "tool" to access the database(?)
        self.db.setDatabaseName("database.db")  #   Adds the database to be accessed
        self.model = QtSql.QSqlTableModel() #   SqlTableModel is automatically connected to the database opened with the code above???
        self.model.setTable("students") #   Sets model to table in the database we want to access
        self.model.select() #   Selects all data on table to be displayed

    def initSearchBox(self):
        self.sbProxyModel = custom.CombineColumnsProxyModel(1, 2)   #   Creates the proxy model the search box will use
        self.sbProxyModel.setSourceModel(self.model)    #   Sets the proxy model's source model

        self.searchBox = custom.SearchBox() #   Creates customSearchBox widget
        self.searchBox.setModel(self.sbProxyModel) #   Sets the searchbox's completer's model
        self.layout.addWidget(self.searchBox)   #   adds the widget to the layout

    def initTableView(self):
        self.tvProxyModel = custom.CombineColumnsProxyModel(1, 2)   #   Creates the proxy model the search box will use
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