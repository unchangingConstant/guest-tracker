from PyQt5 import QtGui, QtWidgets, QtCore, QtSql
from PyQt5.QtWidgets import * 

class SBCompleter(QtWidgets.QCompleter):
    def __init__(self):
        super(SBCompleter, self).__init__()

class AddVisitsWidget(QtWidgets.QWidget):
    def __init__(self, studentModel: QtSql.QSqlTableModel, visitModel: QtSql.QSqlTableModel): 
        super(AddVisitsWidget, self).__init__()

        '''
        Something about storing the database models in the widget feels like bad practice. I don't know why.
        '''
        self.studentModel = studentModel
        self.visitModel = visitModel

        self.layout = QtWidgets.QHBoxLayout()
        self.setLayout(self.layout)

        self.__initSearchComboBox()
        self.__initVisitButton()

    def __initSearchComboBox(self):
        self.searchCombo = QtWidgets.QComboBox()    #   Creates the comboBox from which user will access student names
        self.searchCombo.setInsertPolicy(QtWidgets.QComboBox.InsertPolicy.NoInsert) #   Items in the combo box can't be editted
        self.layout.addWidget(self.searchCombo)    #   Adds widget to layout
        self.searchCombo.setModel(self.studentModel)   #   sets the comboBox's model to the proxy model for the SQL DB, which concatenates the first and last names of each students and returns them
        self.searchCombo.setModelColumn(1)
        self.searchCombo.setCurrentIndex(-1)    #   When program starts, combo box will display placeholder text

    """
    Student search will be implemented at a later date
    """
    def __initCompleter(self):
        self.completer = QCompleter()   #   Sets up completer
        self.completer.setFilterMode(QtCore.Qt.MatchFlag.MatchContains) #   Completer will match based of if keyword is contained in string
        self.completer.setCaseSensitivity(False)    #   Completer is not case-sensitive
        self.completer.setCompletionMode(QtWidgets.QCompleter.CompletionMode.PopupCompletion)   #   Completions popup
        self.completer.setModel(self.sbProxyModel)  #   Sets completer model to the proxy model
        self.searchCombo.setCompleter(self.completer)   #   Searchbar's completer to self.completer

    def __initVisitButton(self):    #   Click this button, and it initiates a visit tied to the currently selected student in the comboBox 
        self.visitButton = QtWidgets.QPushButton("Start Visit") #   Creates button with text
        self.visitButton.clicked.connect(self.addVisit) #   Connects the click of the button to the addVisit function
        self.layout.addWidget(self.visitButton)    #   Adds widget to layout

    def addVisit(self):
        '''
        Read below # comment before this:
        This is why I will not implement the completer yet. It is more important to me to get the addVisits feature up and running for now.
        '''
        studentID = self.studentModel.data(self.studentModel.index(self.searchCombo.currentIndex(), 0)) #   Fetches the ID of the selected student using their index within the comboBox
        record = self.visitModel.record()   #   creates an empty record object ready to be added to the histories table

        record.setValue("id", studentID)    #   This whole block should be self-explanatory
        record.setValue("startTime", "12:00 AM")
        print(self.visitModel.insertRecord(-1, record))   #   Adds at index -1 to append the record to end of table

class VisitsDisplay(QtWidgets.QListView):   #   Made to display the names, startTimes, and durations of all student visits, will comment further when fleshed out more
    def __init__(self, visitingStudentsModel: QtSql.QSqlRelationalTableModel):
        super(VisitsDisplay, self).__init__()

        self.setModel(visitingStudentsModel)
        self.setEditTriggers(QtWidgets.QAbstractItemView.NoEditTriggers)

class ExcludeOnConditionProxyModel(QtCore.QSortFilterProxyModel):
    def __init__(self, fieldName: str, exclusionQualifier: any, parent = None):
        super.__init__(parent)
    
    def mapToSource(self, proxyIndex: QtCore.QModelIndex):  
        return self.sourceModel().index(proxyIndex.row(), self.columnList[0])
    
    def mapFromSource(self, sourceIndex: QtCore.QModelIndex):  
        return self.index(sourceIndex.row(), 0)

class CombineColumnsProxyModel(QtCore.QAbstractProxyModel): #   Proxy model takes a table and returns another table with any number of columns concatenated
    def __init__(self, columnList: list[int], parent = None):
        super().__init__(parent)
        self.columnList = columnList

    def mapToSource(self, proxyIndex: QtCore.QModelIndex):  #   Passes the proxy index and returns the source index "equivalent"
        return self.sourceModel().index(proxyIndex.row(), self.columnList[0])   #   All proxyIndices get mapped to the 1st source model column

    def mapFromSource(self, sourceIndex: QtCore.QModelIndex):   #   Same as mapToSource, but in reverse
        return self.index(sourceIndex.row(), 0) #   All sourceRows get mapped to the proxy's one existing column

    def rowCount(self, parent): #   Wat does parent mean
        return self.sourceModel().rowCount(parent)  #   Has just as many rows as the source

    def columnCount(self, parent: QtCore.QModelIndex):   
        return 1    #   Proxy only has one column: Strings of first and last names

    def index(self, row: int, column: int, parent = QtCore.QModelIndex()):
        if self.hasIndex(row, column, parent):
            return self.createIndex(row, column, self.sourceModel().index(row, self.columnList[0])) #   I have no clue what index()'s third parameter does. Literally no clue
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
        if role == QtCore.Qt.DisplayRole or role == QtCore.Qt.EditRole:
            concatString = ""
            for column in self.columnList:
                concatString = f"{concatString} {self.sourceModel().data(self.sourceModel().index(index.row(), column))}"
            
            return concatString
        '''
        And it worked.
        It makes very little sense to me, why would it need to access the same data twice???
        '''
        #   Future Ethan here!
        #   Turns out, EditRole is the data used to do completions, DisplayRole is the data that's displayed!
        #   (In fact, it says so explicitly in QCompleter's documentation, you donut!)
        #   You're welcome, future Ethan out!
        '''
        (Because in my debugging I found it would pass both DisplayRole and EditRole for every index it accessed)
        I have very little knowledge of how data() is accessed by widgets, and the docs don't seem to say anything about it
        This has made debugging and learning about this module nigh impossible
        But it works
        So
        If any soul is out there reading this and knows where I could find the info I seek, please do let me know.
        '''
        return None
        
        