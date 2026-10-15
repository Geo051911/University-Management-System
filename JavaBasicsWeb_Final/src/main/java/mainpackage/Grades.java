package mainpackage;

public class Grades {

    private final Students student;
    private final Courses course;

    private double grade;

    public Grades(Students student,
                  Courses course,
                  double grade) {

        this.student = student;
        this.course = course;
        this.grade = grade;

        student.addGrade(this);
    }

    public Students getStudent() {
        return student;
    }

    public Courses getCourse() {
        return course;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}