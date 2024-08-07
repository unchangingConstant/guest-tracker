import sys
import os
from PyQt5 import QtCore, QtWidgets, QtSql, Qt
import customWidgets as custom
import datetime as dt

abspath = os.path.abspath(__file__) #   Takes current program path
dirname = os.path.dirname(abspath)  #   Gets current folder where program is
dirname = os.path.join(dirname, "userdata") #   Creates path for the folder "userData"

class TestApp(QtWidgets.QWidget):
    def __init__(self):
        super().__init__()

        self.layout = QtWidgets.QVBoxLayout()
        self.setLayout(self.layout) #   Sets testApp layout

        self.__initModels()
        self.__initAddVisitsWidget()
        self.__initVisitsDisplay()

    def __initModels(self):
        self.studentDB = QtSql.QSqlDatabase().addDatabase("QSQLITE")  #   Opens a sort of "tool" to access the database(?)
        self.studentDB.setDatabaseName(os.path.join(dirname, "database.db"))  #   Adds the database to be accessed

        self.studentModel = QtSql.QSqlTableModel() #   SqlTableModel is automatically connected to the database opened with the code above???
        self.studentModel.setEditStrategy(QtSql.QSqlTableModel.OnFieldChange)   #   "All changes to model will be applied immediately to the database"
        self.studentModel.setTable("students") #   Sets model to table in the database we want to access
        self.studentModel.select() #   Selects all data on table to be displayed

        self.visitModel = QtSql.QSqlTableModel()
        self.visitModel.setEditStrategy(QtSql.QSqlTableModel.OnFieldChange)
        self.visitModel.setTable("histories")
        self.visitModel.select()
        """
        Bottom line explained for my sake:
        QSqlTableModel does not detect changes made directly to the database. However, if insertRecord is called, it will update accordingly.
        If other models are connected to the database, they need to be updated manually anytime other models make changes directly to the database.
        Hence, the line below.

        Given this, I'd also like to create a custom database structure so that I don't have to manually implement these updates.
        Additionally, functions like addVisit should ideally be comparmentalized with model components.
        Though, at this point it seems unessential. So, I'll save it for a later date.

        note TO FUTURE SELF: Consider delegating the below task to the View's QStyledItemDelegate. Look at this sample function to know what I'm talking about:

        def editorEvent(self, event: QtCore.QEvent, model: QtCore.QAbstractItemModel, option: QtWidgets.QStyleOptionViewItem, index: QtCore.QModelIndex): 
        if event.type() == QtCore.QEvent.MouseButtonRelease:    
            if option.rect.adjusted(option.rect.width() - 80, 0, 0, 0).contains(event.pos()):   
                model.removeRow(index.row())
                model.select()
                return True
        return super().editorEvent(event, model, option, index)
        """
        self.visitModel.dataChanged.connect(lambda: self.visitingStudents.select())

        self.visitingStudents = QtSql.QSqlRelationalTableModel()
        self.visitingStudents.setEditStrategy(QtSql.QSqlTableModel.OnFieldChange)
        self.visitingStudents.setTable("histories")
        self.visitingStudents.setRelation(0, QtSql.QSqlRelation("students", "id", "student_name"))
        self.visitingStudents.setFilter("endTime IS NULL")
        self.visitingStudents.select()

    def __initAddVisitsWidget(self):  #   Read all the info you seek in customWidgets.AddVisitsWidget()
        self.addVisitsWidget = custom.AddVisitsWidget()
        self.layout.addWidget(self.addVisitsWidget)
        self.addVisitsWidget.setComboBoxModel(self.studentModel)
        self.addVisitsWidget.connectToClickedSignal(lambda: self.addVisit(self.addVisitsWidget.getComboBoxData()))

    def __initVisitsDisplay(self):
        self.visitsDisplay = custom.ButtonedTableView()
        self.visitsDisplay.setButtonText("End Visit")
        self.visitsDisplay.setModel(self.visitingStudents)
        self.visitsDisplay.setButtonColumn(3)
        self.layout.addWidget(self.visitsDisplay)

    def addVisit(self, studentID: int): #   Takes studentID
        record = self.visitModel.record()   #   creates an empty record object ready to be added to the histories table

        record.setValue("id", studentID)    #   This whole block should be self-explanatory
        record.setValue("startTime", f"{dt.datetime.now()}")
        print(self.visitModel.insertRecord(-1, record))   #   Adds at index -1 to append the record to end of table

if __name__ == "__main__":
    app = QtWidgets.QApplication([])

    window = TestApp()
    window.resize(800, 600)
    window.show()

    sys.exit(app.exec())