from PyQt5 import QtCore, QtGui
from PyQt5 import QtWidgets

#Creates a searchable comboBox that autocompletes your search
class SearchBox(QtWidgets.QComboBox):
    def __init__(self):
        super(SearchBox, self).__init__()

        #sets up search functionalities
        self.setEditable(True)
        self.completer = QtWidgets.QCompleter(self)
        self.completer.setCompletionMode(QtWidgets.QCompleter.UnfilteredPopupCompletion)
        #This doesn't work as intended
        #womp
        self.completer.setFilterMode(QtCore.Qt.MatchContains)

        #aesthetic stuff
        self.setPlaceholderText("")
        self.setCurrentIndex(-1)

    def addItems(self, strList):
        super(SearchBox, self).addItems(strList)
        self.setPlaceholderText("")
        self.setCurrentIndex(-1)