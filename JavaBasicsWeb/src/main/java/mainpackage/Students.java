package mainpackage;

import java.util.ArrayList;

public class Students extends Users {

    private final int registrationNumber;
    private ArrayList<Grades> grades;

    public Students(
            String username,
            String name,
            String surname,
            String department,
            int registrationNumber) {

        super(username, name, surname, department);

        this.registrationNumber = registrationNumber;
        this.grades = new ArrayList<>();
    }

    public int getRegistrationNumber() {
        return registrationNumber;
    }

    public void addGrade(Grades grade) {
        grades.add(grade);
    }

    public ArrayList<Grades> getGrades() {
        return grades;
    }

    public void displayGrades() {

        if (grades.isEmpty()) {
            System.out.println("No grades available.");
        } else {

            for (Grades grade : grades) {

                System.out.println(
                        "Course: "
                                + grade.getCourse().getCourseName()
                );

                System.out.println(
                        "Grade: " + grade.getGrade()
                );
            }
        }
    }
}