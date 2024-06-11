#Handles all database operations
import sqlite3
import os

abspath = os.path.abspath(__file__)
dirname = os.path.dirname(abspath)
dirname = os.path.join(dirname, "userdata")
os.chdir(dirname)

class Database():

    def __init__(self):
        self._database = sqlite3.connect("database.db")
        self._cursor = self._database.cursor()

    def fetchStudentStrings(self) -> list[tuple]:
        self._cursor.execute("SELECT * FROM students")
        rawStudentInfo = self._cursor.fetchall()
        studentStrings = []
        
        for student in rawStudentInfo:
            studentStr = f"{student[0]}: {student[1]} {student[2]}"
            studentStrings.append(studentStr)

        return studentStrings

students = Database()
students.fetchStudentStrings()