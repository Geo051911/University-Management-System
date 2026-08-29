package mainpackage;

import java.util.ArrayList;

public class Professors extends Users {

    private final int professorNumber;
    private ArrayList<Courses> courses;

    public Professors(String username, String name,
                      String surname, String department,
                      int professorNumber) {

        super(username, name, surname, department);

        this.professorNumber = professorNumber;
        this.courses = new ArrayList<>();
    }

    public int getProfessorNumber() {
        return professorNumber;
    }

    public void addCourse(Courses course) {
        courses.add(course);
    }

    public void showCourses() {

        if (courses.isEmpty()) {
            System.out.println("The professor has no courses.");
        } else {

            for (Courses course : courses) {
                System.out.println(course.getCourseName());
            }
        }
    }
    public void setGrade(Students student,
            Courses course,
            double grade) {
        if (!courses.contains(course)) {
            System.out.println("The professor does not teach this course.");
            return;
        }
        if (!course.getStudents().contains(student)) {
            System.out.println("The student is not enrolled in this course.");
            return;
        }
        new Grades(student, course, grade);
        System  .out.println("Grade set successfully for student " + student.getName() + " in course " + course.getCourseName() + ".");
    }
    public void displayStudentGrades() {

        for (Courses course : courses) {

            System.out.println(
                "Course: " + course.getCourseName()
            );

            for (Students student : course.getStudents()) {

                for (Grades grade : student.getGrades()) {

                    if (grade.getCourse() == course) {

                        System.out.println(
                            "Student: "
                                    + student.getName()
                                    + " "
                                    + student.getSurname()
                        );

                        System.out.println(
                            "Grade: " + grade.getGrade()
                        );
                    }
                }
            }
        }
    }
}
