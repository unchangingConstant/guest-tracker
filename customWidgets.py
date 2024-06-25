from PyQt5 import QtGui, QtWidgets, QtCore, QtSql
from PyQt5.QtWidgets import * 

class SearchBox(QLineEdit): #   Creates a search bar that autocompletes your search
    def __init__(self):
        super(SearchBox, self).__init__()
        
        self.completer = QCompleter()   #   Sets up completer
        self.completer.setFilterMode(QtCore.Qt.MatchFlag.MatchContains) #   Completer will match based of if keyword is contained in string
        self.completer.setCaseSensitivity(False)    #   Completer is not case-sensitive
        self.completer.setCompletionMode(QtWidgets.QCompleter.CompletionMode.PopupCompletion)

        self.completer.activated[QtCore.QModelIndex].connect(lambda x: print(x.row()))

        self.setCompleter(self.completer)   #   Searchbar's completer to self.completer
    
    def setModel(self, model: QtCore.QAbstractProxyModel):
        self.completer.setModel(model)

class CombineColumnsProxyModel(QtCore.QAbstractProxyModel):
    def __init__(self, colIndexOne: int, colIndexTwo: int, parent = None):
        super().__init__(parent)
        self.columnOne = colIndexOne
        self.columnTwo = colIndexTwo

    def mapToSource(self, proxyIndex: QtCore.QModelIndex):  #   Passes the proxy index and returns the source index "equivalent"
        return self.sourceModel().index(proxyIndex.row(), self.columnOne)   #   All proxyIndices get mapped to the 1st source model column

    def mapFromSource(self, sourceIndex: QtCore.QModelIndex):   #   Same as mapToSource, but in reverse
        return self.index(sourceIndex.row(), 0) #   All sourceRows get mapped to the proxy's one existing column

    def rowCount(self, parent): #   Wat does parent mean
        return self.sourceModel().rowCount(parent)  #   Has just as many rows as the source
    
    def columnCount(self, parent: QtCore.QModelIndex):   
        return 1    #   Proxy only has one column: Strings of first and last names

    def index(self, row: int, column: int, parent = QtCore.QModelIndex()):
        if self.hasIndex(row, column, parent):
            return self.createIndex(row, column, self.sourceModel().index(row, self.columnOne)) #   I have no clue what index()'s third parameter does. Literally no clue
        return QtCore.QModelIndex() #   Apparently this is an invalid index, and it's supposed to be that way

    def data(self, index: QtCore.QModelIndex, role = QtCore.Qt.DisplayRole):
        if not index.isValid():
            return None
        '''
        Originally, I couldn't get this method to work.
        If I only accounted for DisplayRole being passed the Completer wouldn't show anything
        Through print statement debugging I found out QCompleter was passing EditRole to role
        I accounted for only EditRole and it would just pull up a blank list
        I spent 2 hours on this before consulting ChatGPT, which suggested I simply account for both like so:
        '''
        if role ==  QtCore.Qt.EditRole or role == QtCore.Qt.DisplayRole:
            firstName = self.sourceModel().data(self.sourceModel().index(index.row(), self.columnOne))
            lastName = self.sourceModel().data(self.sourceModel().index(index.row(), self.columnTwo))
            return f"{firstName} {lastName}"
        '''
        And it worked.
        It makes very little sense to me, why would it need to access the same data twice???
        (Because in my debugging I found it would pass both DisplayRole and EditRole for every index it accessed)
        I have very little knowledge of how data() is accessed by widgets, and the docs don't seem to say anything about it
        This has made debugging and learning about this module nigh impossible
        But it works
        So
        If any soul is out there reading this and knows where I could find the info I seek, please do let me know.
        '''
        return None
        
        