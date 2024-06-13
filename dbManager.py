#Handles all database operations
import sqlite3
import os

#Sets current working directory to the "userdata" file in project's root directory
abspath = os.path.abspath(__file__)
dirname = os.path.dirname(abspath)
dirname = os.path.join(dirname, "userdata")
os.chdir(dirname)

class Database():

    #Connects object to database and sets up cursor
    def __init__(self):
        self._database = sqlite3.connect("database.db")
        self._cursor = self._database.cursor()

    #Fetches a list of strings with students' info this format: 
    # "{4 digit ID}: {student full name}"
    def fetchStudentStrings(self) -> list[tuple]:
        self._cursor.execute("SELECT * FROM students")
        rawStudentInfo = self._cursor.fetchall()
        studentStrings = []
        
        for student in rawStudentInfo:
            studentStr = f"{student[0]}: {student[1]} {student[2]}"
            studentStrings.append(studentStr)

        return studentStrings
    
    #Method was added to connect to certain widget signals
    #Helps test anything related to the gui modifying database info
    '''
    def toggleDaddy(self, toggled: bool):
        if toggled:
            self._cursor.execute("INSERT INTO students VALUES (1005, 'Begley', 'Daddy')")
            self._cursor.execute("COMMIT")
        else:
            self._cursor.execute("DELETE FROM students WHERE id = '1005'")
            self._cursor.execute("COMMIT")
    '''
