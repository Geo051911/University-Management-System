package mainpackage;
import java.util.ArrayList;

public class Secretaries extends Users {

    private final int employeeNumber;

    public Secretaries(String username, String name,
                       String surname, String department,
                       int employeeNumber) {

        super(username, name, surname, department);

        this.employeeNumber = employeeNumber;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public Students createStudent(
            String username,
            String name,
            String surname,
            String department,
            int registrationNumber) {

        return new Students(
                username,
                name,
                surname,
                department,
                registrationNumber
        );
    }

    public Professors createProfessor(
            String username,
            String name,
            String surname,
            String department,
            int professorNumber) {

        return new Professors(
                username,
                name,
                surname,
                department,
                professorNumber
        );
    }

    public Courses createCourse(
            String courseCode,
            String courseName) {

        return new Courses(courseCode, courseName);
    }

    public void assignProfessorToCourse(
            Professors professor,
            Courses course) {

        course.setProfessor(professor);
        professor.addCourse(course);
    }

    public void addStudentToCourse(
            Students student,
            Courses course) {

        course.addStudent(student);
    }
    public ArrayList<Students> createGradingList(Courses course) {
        return course.getStudents();
    }
}