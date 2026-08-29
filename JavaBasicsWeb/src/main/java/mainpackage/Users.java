package mainpackage;

public class Users {

    private String username;
    private String name;
    private String surname;
    private String department;

    private static int usersCounter = 0;

    public Users(String username, String name,
                 String surname, String department) {

        this.username = username;
        this.name = name;
        this.surname = surname;
        this.department = department;

        usersCounter++;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public static int getUsersCounter() {
        return usersCounter;
    }
}