from PyQt5 import QtGui, QtWidgets, QtCore, QtSql
import datetime as dt

class AddVisitsWidget(QtWidgets.QWidget):
    def __init__(self): 
        super(AddVisitsWidget, self).__init__()

        self.layout = QtWidgets.QHBoxLayout()
        self.setLayout(self.layout)

        self.__initSearchComboBox()
        self.__initVisitButton()

    def __initSearchComboBox(self):
        self.searchCombo = QtWidgets.QComboBox()    #   Creates the comboBox from which user will access student names
        self.searchCombo.setInsertPolicy(QtWidgets.QComboBox.InsertPolicy.NoInsert) #   Items in the combo box can't be editted
        self.layout.addWidget(self.searchCombo)    #   Adds widget to layout

        self.searchCombo.setCurrentIndex(-1)    #   When program starts, combo box will display placeholder text

    def __initVisitButton(self):    #   Click this button, and it initiates a visit tied to the currently selected student in the comboBox 
        self.visitButton = QtWidgets.QPushButton("Start Visit") #   Creates button with text
        self.layout.addWidget(self.visitButton)    #   Adds widget to layout
    
    def setComboBoxModel(self, model: QtCore.QAbstractTableModel):  #Sets comboBox's model. Model is not tracked within widget, this is to compartmentalize model/view components
        comboProxyModel = CorrespondingUserRoleProxyModel(0, 1)
        comboProxyModel.setSourceModel(model)
        self.searchCombo.setModel(comboProxyModel)

    def connectToClickedSignal(self, function): #   Connects a function to the button within the widget. (As far as I know, the function you connect must be passed as a lambda function)
        self.visitButton.clicked.connect(function)
    
    def getComboBoxData(self):  #   Fetches current selected data in comboBox, with role QtCore.Qt.UserRole (By default the comboBox returns data with that role but I want to specify)
        return self.searchCombo.currentData()

class VisitsDisplay(QtWidgets.QListView):   #   Made to display the names, startTimes, and durations of all student visits, will comment further when fleshed out more
    def __init__(self):
        super(VisitsDisplay, self).__init__()
        self.setEditTriggers(QtWidgets.QAbstractItemView.NoEditTriggers)

'''
Made for use by comboBoxes
When instantiating this proxy model, you pass two integers as arguments.
The column from the sourceModel that you want to be the userRole, and the column that you want to be the displayRole
    These two columns will be paired up accordingly in the comboBox
    
The comboBox will display items in the displayRole column
When currentData() is called, it will return items in the userRole column
'''
class CorrespondingUserRoleProxyModel(QtCore.QAbstractProxyModel):  #   Meant to map from table model into list-like model

    def __init__(self, userRoleCol: int, displayRoleCol: int):
        super().__init__()
        self.userRoleColumn = userRoleCol
        self.displayRoleColumn = displayRoleCol
    
    def mapToSource(self, proxyIndex: QtCore.QModelIndex):
        #   If the proxyIndex is 0 or 1, it returns either the userRole data or the displayRole data, respectively.
        #   Otherwise, returns the userRole column
        if (proxyIndex.column() == 0):
            return self.sourceModel().index(proxyIndex.row(), self.userRoleColumn)
        if (proxyIndex.column() == 1):
            return self.sourceModel().index(proxyIndex.row(), self.displayRoleColumn)
        else:
            return self.sourceModel().index(proxyIndex.row(), 0)

    def mapFromSource(self, sourceIndex: QtCore.QModelIndex): 
        #   If the sourceIndex corresponds to the columns selected to be either userRole or displayRole, the proxy model will return the corresponding index
        #   Otherwise, returns the userRole data
        if (sourceIndex.column == self.userRoleColumn):
            return self.index(sourceIndex.row(), 0)
        if (sourceIndex.column == self.displayRoleColumn):
            return self.index(sourceIndex.row(), 1)
        else:
            return self.index(sourceIndex.row(), 0)
    
    def rowCount(self, parent: QtCore.QModelIndex):
        return self.sourceModel().rowCount(parent)  #   Row count is same as sourceModel's

    def columnCount(self, parent: QtCore.QModelIndex):
        return 2    #   This proxy model will only ever have two columns

    def index(self, row: int, column: int, parent = QtCore.QModelIndex()):
        if self.hasIndex(row, column, parent):  #   I'm also not sure how hasIndex works
            return self.createIndex(row, column, self.sourceModel().index(row, column)) #   Again, I have no idea what this 3rd parameter is supposed to do
        return QtCore.QModelIndex() #   Apparently this is an invalid index, and it's supposed to be that way

    def data(self, index: QtCore.QModelIndex, role = QtCore.Qt.DisplayRole):
        if not index.isValid():
            return None
        
        if role == QtCore.Qt.DisplayRole: #   Returns the selected displayRole column is either DisplayRole or EditRole is called for
            return self.sourceModel().data(self.sourceModel().index(index.row(), self.displayRoleColumn))
        if role == QtCore.Qt.UserRole:  #   Returns the selected userRole column if UserRole is called for
            return self.sourceModel().data(self.sourceModel().index(index.row(), self.userRoleColumn))
        
        return None

class ElapsedTimeProxyModel(QtCore.QAbstractProxyModel):
    def __init__(self, startCol: int, displayCol: int):
        super(ElapsedTimeProxyModel, self).__init__()
        self.startTime = startCol
        self.displayColumn = displayCol

    def rowCount(self, parent = QtCore.QModelIndex()):
        return self.sourceModel().rowCount(parent) 

    def columnCount(self, parent = QtCore.QModelIndex()):
        return self.sourceModel().columnCount(parent) 

    def mapToSource(self, proxyIndex: QtCore.QModelIndex) -> QtCore.QModelIndex:
        return self.sourceModel().index(proxyIndex.row(), proxyIndex.column())

    def mapFromSource(self, sourceIndex: QtCore.QModelIndex) -> QtCore.QModelIndex:
        return self.index(sourceIndex.row(), sourceIndex.column())
    
    def index(self, row: int, column: int, parent = QtCore.QModelIndex()):
        if self.hasIndex(row, column, parent):  #   I'm not sure how hasIndex works
            return self.createIndex(row, column)
        return QtCore.QModelIndex() 

    def data(self, index: QtCore.QModelIndex, role = QtCore.Qt.DisplayRole):
        data = super().data(index, role)
        if index.column() == self.displayColumn:
            elapsedTime = self.__findElapsedTime(self.sourceModel().data(self.sourceModel().index(index.row(), self.startTime)))
            return elapsedTime
        return data
    
    def __findElapsedTime(self, datetime: str) -> int: #   datetime needs to be in the format given by str(datetime.datetime) 
        currentTime = dt.datetime.strptime(datetime, '%Y-%m-%d %H:%M:%S.%f')
        timeElapsed = dt.datetime.now() - currentTime
        return int(timeElapsed.total_seconds() / 60)

class ButtonedTableView(QtWidgets.QTableView):
    def __init__(self):
        super(ButtonedTableView, self).__init__()
        self.setItemDelegate(ButtonedTableView.ButtonedTableDelegate()) #   Sets the delegate to the custom one made solely for this widget
        self.setEditTriggers(QtWidgets.QAbstractItemView.NoEditTriggers)
        self.horizontalHeader().setSectionResizeMode(QtWidgets.QHeaderView.ResizeMode.Stretch)  #   Table fills available width in layout
    
    def setButtonFunction(self, function):  #   Connects the button in the item delegate to a function. Note: self.itemDelegate().buttonFunctionPassColumn will be passed to the connected function
        self.itemDelegate().setButtonFunction(function) #   Comments are fun!

    def setButtonFunctionPassedColumn(self, column: int):
        self.itemDelegate().setButtonFunctionPassedColumn(column)
    
    def setButtonColumn(self, column: int):
        self.itemDelegate().setButtonColumn(column)

    def setButtonText(self, text: str):
        self.itemDelegate().setButtonText(text)

    def setModel(self, model: QtCore.QAbstractItemModel):
        self.proxyModel = ElapsedTimeProxyModel(1, 2)
        self.proxyModel.setSourceModel(model)
        super().setModel(self.proxyModel)

    class ButtonedTableDelegate(QtWidgets.QStyledItemDelegate):  #  I've nested the class since I don't foresee this class being useful under any other circumstance
        def __init__(self):
            super(ButtonedTableView.ButtonedTableDelegate, self).__init__()
            self.buttonFunction = None
            self.buttonFunctionCol = 0
            self.buttonCol = 0
            self.buttonText = ""
        
        def setButtonFunction(self, function):
            self.buttonFunction = function
        
        def setButtonFunctionPassedColumn(self, column: int):
            self.buttonFunctionCol = column

        def setButtonColumn(self, column: int):
            self.buttonCol = column

        def setButtonText(self, text):
            self.buttonText = text

        def paint(self, painter: QtGui.QPainter, option: QtWidgets.QStyleOptionViewItem, index: QtCore.QModelIndex):    #   Paint function paints the item from the model, gotcha
            if index.column() == index.model().columnCount() - 1:   #   If the item being drawn is at the specified index
                button_style = QtWidgets.QStyleOptionButton()
                button_style.rect = option.rect.adjusted(option.rect.width(), 0, 0, 0)
                button_style.text = self.buttonText
                QtWidgets.QApplication.style().drawControl(QtWidgets.QStyle.CE_PushButton, button_style, painter)
            else:
                super().paint(painter, option, index)
        """
        Explanation of editor event:
        All mouse events are passed to editorEvent. It then decides whether it "starts" an editting event and acts accordingly.
        For example:
            Hovering your mouse over the QListView is passed to editorEvent, which subsequently highlights the item in listView
        Basically a catch-all function for anything that happens that concerns the assigned view
        Oh ChatGPT my lord and savior
        """
        def editorEvent(self, event: QtCore.QEvent, model: QtCore.QAbstractItemModel, option: QtWidgets.QStyleOptionViewItem, index: QtCore.QModelIndex): #   Function explained above
            if event.type() == QtCore.QEvent.MouseButtonRelease:    #   From my understanding, MouseButtonRelease is the combined clicking and releasing of the button
                if index.column() == self.buttonCol:   #   Checks that the event occured where the painted button is
                    try:
                        self.buttonFunction(model.data(index, self.buttonFunctionDataRole))
                        return True
                    except:
                        print("Error with ButtonedListDelegate's buttonFunction")
            return super().editorEvent(event, model, option, index)