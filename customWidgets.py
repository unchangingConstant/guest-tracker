from PyQt5 import QtGui, QtWidgets, QtCore, QtSql
from PyQt5.QtWidgets import * 

#Creates a search bar that autocompletes your search
class SearchBox(QLineEdit):
    def __init__(self):
        super(SearchBox, self).__init__()
        
        self.completer = QCompleter()   #Sets up completer

        self.completer.setFilterMode(QtCore.Qt.MatchFlag.MatchContains) #Completer will match based of if keyword is contained in string
        self.completer.setCaseSensitivity(False)    #Completer is no case-sensitive
        self.setCompleter(self.completer)   #Searchbar's completer to self.completer
    
    def setCompleterModel(self, model: QtSql.QSqlTableModel):   #Sets the completer's model to the passed SqlTableModel
        self.completer.setModel(model)