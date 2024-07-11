from PyQt5 import QtGui, QtWidgets, QtCore, QtSql
from PyQt5.QtWidgets import * 

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
class CorrespondingUserRoleProxyModel(QtCore.QAbstractProxyModel):

    def __init__(self, userRoleCol: int, displayRoleCol: int, parent = None):
        super().__init__(parent)
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
    
    def rowCount(self, parent):
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
        
        if role == QtCore.Qt.DisplayRole or role == QtCore.Qt.EditRole: #   Returns the selected displayRole column is either DisplayRole or EditRole is called for
            return self.sourceModel().data(self.sourceModel().index(index.row(), self.displayRoleColumn))
        if role == QtCore.Qt.UserRole:  #   Returns the selected userRole column if UserRole is called for
            return self.sourceModel().data(self.sourceModel().index(index.row(), self.userRoleColumn))
        
        return None
    
class WidgetPerItemListView(QtWidgets.QListView):    #   A list view that keeps widgets for each item
    def __init__(self):
        super(WidgetPerItemListView, self).__init__()
        self.widgetPerItem = []
    
    def addWidget(self, widget: QtWidgets.QWidget, position: int):    #   Allows you to add a widget to be added next to each item on the view
        self.widgetPerItem.insert(position, widget)
        self.__updateWidgets()

    def __updateWidgets(self):   #   This function will add all stored widgets per item

        for i in range(self.model().rowCount()):
            for widget in self.widgetPerItem:
                self.setIndexWidget(self.model().index(i, 0), widget())
    
    def setModel(self, model: QtCore.QAbstractItemModel):
        super().setModel(model)
        model.dataChanged.connect(lambda: self.__updateWidgets())



    

    