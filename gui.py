# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'designerTest.ui'
#
# Created by: PyQt5 UI code generator 5.15.9
#
# WARNING: Any manual changes made to this file will be lost when pyuic5 is
# run again.  Do not edit this file unless you know what you are doing.


from PyQt5 import QtCore, QtGui, QtWidgets
from dbManager import *
import customWidgets as custom


class Ui_MainWindow(object):
    def setupUi(self, MainWindow):

        self._dbRef = Database()

        MainWindow.setObjectName("MainWindow")
        MainWindow.resize(899, 541)

        self.createLayOut()
        self.createSearchBox()

        self.listView = QtWidgets.QListView(self.centralwidget)
        self.listView.setItemAlignment(QtCore.Qt.AlignLeading)
        self.listView.setObjectName("listView")
        self.verticalLayout.addWidget(self.listView)

        self.verticalLayout_2.addLayout(self.verticalLayout)
        MainWindow.setCentralWidget(self.centralwidget)
        self.menubar = QtWidgets.QMenuBar(MainWindow)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 899, 21))
        self.menubar.setObjectName("menubar")
        MainWindow.setMenuBar(self.menubar)
        self.statusbar = QtWidgets.QStatusBar(MainWindow)
        self.statusbar.setObjectName("statusbar")
        MainWindow.setStatusBar(self.statusbar)

        self.retranslateUi(MainWindow)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)

    def createLayOut(self):
        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.verticalLayout_2 = QtWidgets.QVBoxLayout(self.centralwidget)
        self.verticalLayout_2.setObjectName("verticalLayout_2")
        self.verticalLayout = QtWidgets.QVBoxLayout()
        self.verticalLayout.setObjectName("verticalLayout")

    def createSearchBox(self):
        #SearchBox shtuff  
        self.comboBox = custom.SearchBox()
        self.comboBox.addItems(self._dbRef.fetchStudentStrings())
        self.verticalLayout.addWidget(self.comboBox)

    def retranslateUi(self, MainWindow):
        _translate = QtCore.QCoreApplication.translate
        MainWindow.setWindowTitle(_translate("MainWindow", "MainWindow"))


if __name__ == "__main__":
    import sys
    app = QtWidgets.QApplication(sys.argv)
    MainWindow = QtWidgets.QMainWindow()
    ui = Ui_MainWindow()
    ui.setupUi(MainWindow)
    MainWindow.show()
    sys.exit(app.exec_())
