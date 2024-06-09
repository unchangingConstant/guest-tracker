#A file with sample code for the sqlite3 module for my reference

import sqlite3
import os

abspath = os.path.abspath(__file__)
dname = os.path.dirname(abspath)
os.chdir(dname)

def createDB():
    # define connection and cursor

    #This is the database
    connection = sqlite3.connect("database.db")

    #Used to interact with database
    cursor = connection.cursor()

    #Creates student table I guess
    command1 = """CREATE TABLE IF NOT EXISTS 
    classroom(student_id INT PRIMARY KEY, name TEXT)"""
    cursor.execute(command1)

    #Inserts stuff into table
    cursor.execute("INSERT INTO classroom VALUES (1001, 'Begley, Ethan')")
    cursor.execute("INSERT INTO classroom VALUES (1002, 'Begley, Luna')")
    cursor.execute("INSERT INTO classroom VALUES (1003, 'Begley, Gwyn')")
    connection.commit()

    #fetches things from table
    cursor.execute("SELECT * FROM classroom")
    results = cursor.fetchall()
    print(results)

def addToDB():
    connection = sqlite3.connect("database.db")
    cursor = connection.cursor()

    value = 1001
    firstName = ""
    lastName = ""

    cursor.execute(f"INSERT INTO classroom VALUES ({value}, '{lastName}, {firstName}')")
    connection.commit()

def displayDB():
    connection = sqlite3.connect("database.db")
    cursor = connection.cursor()

    cursor.execute("SELECT * FROM classroom")
    results = cursor.fetchall()
    print(results)