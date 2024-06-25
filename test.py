import customWidgets as custom
import gui
from PyQt5 import QtWidgets, QtCore

app = QtWidgets.QApplication([])
testApp = gui.TestApp()

print(testApp.sbProxyModel.data(testApp.model.index(0, 0)))
print(testApp.model.data(testApp.model.index(0, 0)))
print(QtCore.Qt.DisplayRole)