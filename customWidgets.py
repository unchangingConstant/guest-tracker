from PyQt5 import QtCore, QtGui
from PyQt5 import QtWidgets

#Creates a search bar that autocompletes your search

class SearchBox(QtWidgets.QLineEdit):
    def __init__(self):
        super(SearchBox, self).__init__()

        #I don't know what this does
        self.model = QtCore.QStringListModel()
        
        #Sets up completer
        self.completer = QtWidgets.QCompleter()
        #I don't know what this does
        self.completer.setModel(self.model)
        #Completer matches if string in list contains current types word
        #It's also not case sensitive
        self.completer.setFilterMode(QtCore.Qt.MatchFlag.MatchContains)
        self.completer.setCaseSensitivity(False)
        self.setCompleter(self.completer)
    
    #Sets searchable list of items
    def addItems(self, strList):
        self.model.setStringList(strList)