from PyQt5 import QtGui, QtWidgets, QtCore, QtSql
from PyQt5.QtWidgets import * 

class SearchBox(QLineEdit): #   Creates a search bar that autocompletes your search
    def __init__(self):
        super(SearchBox, self).__init__()
        
        self.completer = QCompleter()   #   Sets up completer
        self.completer.setFilterMode(QtCore.Qt.MatchFlag.MatchContains) #   Completer will match based of if keyword is contained in string
        self.completer.setCaseSensitivity(False)    #   Completer is no case-sensitive
        self.completer.setCompletionMode(QtWidgets.QCompleter.CompletionMode.PopupCompletion)
        self.completer.setCompletionColumn(1)

        self.setCompleter(self.completer)   #   Searchbar's completer to self.completer
    
    def setModel(self, model: QtSql.QSqlTableModel):
        self.completer.setModel(model)