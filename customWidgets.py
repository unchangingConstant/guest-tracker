from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import * 

#Creates a search bar that autocompletes your search
class SearchBox(QLineEdit):
    def __init__(self):
        super(SearchBox, self).__init__()

        #I don't know what this does
        self.model = QtCore.QStringListModel()

        #Sets up completer
        self.completer = QCompleter()
        #I don't know what this does
        self.completer.setModel(self.model)
        #Completer matches if string in list contains current types word
        #It's also not case sensitive
        self.completer.setFilterMode(QtCore.Qt.MatchFlag.MatchContains)
        self.completer.setCaseSensitivity(False)
        self.setCompleter(self.completer)

    #Sets searchable list of items
    def addItems(self, strList: list[str]):
        self.model.setStringList(strList)

class ListItem(QListWidgetItem):
    def __init__(self, labelText: str, buttonText: str):
        super(ListItem, self).__init__()

        self.item = QWidget()
        self.label = QLabel(labelText)
        self.button = QPushButton("End Visit")
        #self.label.setFrameStyle(QFrame.StyledPanel | QFrame.Plain)

        self.button = QPushButton(buttonText)

        self.itemLayout = QHBoxLayout()
        self.itemLayout.addWidget(self.label)
        self.itemLayout.addWidget(self.button)
        self.item.setLayout(self.itemLayout)
        self.setSizeHint(self.item.sizeHint())
        #self.button.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Preferred)


        self.addWidget(self.label)
        self.addWidget(self.button)

class ButtonList(QListView):
    def __init__(self):
        super(ButtonList, self).__init__()

    #Note to self: create addItems() method too
    def addItem(self, labelText: str, buttonText: str):
        listItem = ListItem(labelText, buttonText)
        self.addItem(listItem)
        self.setItemWidget(listItem, listItem.item)

    #Clears the list of visits
    def clearList(self):
        while self.count():
            child = self.takeAt(0)
            if child.widget():
                child.widget().deleteLater()
    
    
