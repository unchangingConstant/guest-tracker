import sqlite3
from datetime import datetime

class DBManager():

    #   Create cursor and connection to SQLite database
    def __init__(self, fileName: str):
        self._connection = sqlite3.connect(fileName)
        self._cursor = self._connection.cursor()

    #   Checks if attempted added visit overlaps with another visit from same student
    #   If no overlap, adds the visit, returns true
    #   Otherwise, returns false
    #   Also checks that student is in database before adding visit
    def add_visit(self, student_id: int, start_time: str, end_time: str) -> bool:
        
        #   This method shouldn't be used to add ongoing visits
        if end_time is None or start_time is None:
            return False
        
        visits = self.fetch_visit_table()
        students = self.fetch_student_table()

        #   Checks that student ID in visit exists in student table
        if student_id not in students.keys():
            return False

        for visit in visits:    #   Checks if any visits overlap with the one being added
            try:    #   __times_overlap raises an error if string formats are invalid
                times_overlap = self.__times_overlap(start_time, end_time, visit.get("start_time"), visit.get("end_time"))
            except:
                return False
            
            same_student = visit.get("student_id") == student_id
            if (times_overlap and same_student):
                return False

        self._cursor.execute(f"""
            INSERT INTO visits (student_id, start_time, end_time)
            VALUES (?, ?, ?)
        """, (student_id, start_time, end_time))
        return True

    #   Adds a student
    def add_student(self, student_name: str) -> bool:
        new_id = self.create_student_id()

        self._cursor.execute("""
            INSERT INTO students (student_id, student_name)
            VALUES (?, ?);
            """, (new_id, student_name))

    #   Removes a visit from the table
    #   Moves all entries down to fill the gaps
    def remove_visit_entry(self, student: int):
        pass

    #   Generates unique student ID for student table
    def create_student_id(self) -> int:

        self._cursor.execute(f"SELECT MAX(student_id) FROM students")    #   finds largest ID in the student table 
        largest_id = self._cursor.fetchone()[0]

        if (largest_id is None):
            return 100001

        return largest_id + 1
    
    #   Returns a dictionary of all the students and their id numbers.
    def fetch_student_table(self) -> dict:
        self._cursor.execute("SELECT * FROM students")
        students = self._cursor.fetchall()

        if len(students) == 0:
            return None

        student_entries = {}

        for student in students:    #   Creates a dictionary where the key is the student_id
            student_entries.update({student[0]: student[1]})
        
        return student_entries
    
    #   Returns a list of all visits expressed as dictionaries in a list
    def fetch_visit_table(self) -> list[dict]:
        self._cursor.execute("SELECT * FROM visits")
        visit_tuples = self._cursor.fetchall()

        if len(visit_tuples) == 0:
            return None

        visits = []

        for visit in visit_tuples:
            visits.append({"student_id": visit[0], "start_time": visit[1], "end_time": visit[2]})

        return visits

    #   If student exists and they have no ongoing visit, adds an ongoing visit
    def start_visit(self, student_id: int, student_name = "") -> bool:   #   Returns boolean as indicator of successful operation
        
        #   Checks that student doesn't have visit there already
        self._cursor.execute(f"""
            SELECT 1 
            FROM ongoing-visits
            WHERE student_id = ?
            LIMIT 1;
        """, (student_id))

        ongoing_visit = self._cursor.fetchone() is not None

        #   If the result of the search is None, it means no visits are ongoing, and we can create a new one
        if not ongoing_visit:
            start_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            #   stub comment for bit where visit is added to table
            return True

        return False

    #   If given student has ongoing visit, gives ongoing-visit an endtime 
    #       and moves it to the visits table
    def close_visit(self, student_id: int):
        pass

    #   If student exists, remove from table. 
    #   Moves all entries down to fill gaps
    def remove_student(self, student_id: int):
        pass
    
    #   Creates list of visits
    def fetch_active_visits(self) -> list[dict]:
        pass
    
    #   Closes the sqlite connection
    def __del__(self):
        self._cursor.close()
        self._connection.close()

    def __times_overlap(self, start1: str, end1: str, start2: str, end2: str):
        start_time1 = datetime.strptime(start1, "%Y-%m-%d %H:%M:%S")
        start_time2 = datetime.strptime(start2, "%Y-%m-%d %H:%M:%S")
        end_time1 = datetime.strptime(end1, "%Y-%m-%d %H:%M:%S")
        end_time2 = datetime.strptime(end2, "%Y-%m-%d %H:%M:%S")

        return max(start_time1, start_time2) <= min(end_time1, end_time2)
