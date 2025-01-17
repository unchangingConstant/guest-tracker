#Handles all database operations
import sqlite3
import os
from datetime import datetime

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

    def getStudentName(self, studentID: int):
        self._cursor.execute(f"SELECT firstName, lastName FROM students WHERE id = {studentID}")
        name =  self._cursor.fetchone()
        return name[0] + " " + name[1]

    #Adds a visit to the database using only visiting student's studentID and currentTime
    def startVisit(self, studentID: int):

        self._cursor.execute(f"SELECT visiting FROM students WHERE id = {studentID}")
        visiting = self._cursor.fetchone()[0]
        print(visiting)

        if visiting == 0:
            currentTime = str(datetime.now())
            self._cursor.execute(f'INSERT INTO histories VALUES ({studentID}, "{currentTime}", NULL, NULL)')
            self._database.commit()
            print("Visit made!")
        else:
            print("Already visiting!!!")

    def endVisit(self, studentID: int):
        currentTime = str(datetime.now())
        self._cursor.execute(f'UPDATE histories SET endTime = "{currentTime}" WHERE id = {studentID} AND endTime IS NULL')
        self._database.commit()

    def getOngoingVisits(self) -> list[tuple]:
        self._cursor.execute("SELECT * FROM histories WHERE endTime IS NULL")
        ongoingVisits = self._cursor.fetchall()
        return ongoingVisits
