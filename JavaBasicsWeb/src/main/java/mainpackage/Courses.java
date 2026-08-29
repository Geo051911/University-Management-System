										package mainpackage;

import java.util.ArrayList;

public class Courses {

    private final String courseCode;
    private String courseName;

    private Professors professor;
    private ArrayList<Students> students;

    public Courses(String courseCode, String courseName) {

        this.courseCode = courseCode;
        this.courseName = courseName;

        this.students = new ArrayList<>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Professors getProfessor() {
        return professor;
    }

    public void setProfessor(Professors professor) {
        this.professor = professor;
    }

    public void addStudent(Students student) {
        students.add(student);
    }

    public ArrayList<Students> getStudents() {
        return students;
    }
}