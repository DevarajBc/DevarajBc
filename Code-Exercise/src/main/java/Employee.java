/**
 * Represents an employee in the company.
 */
public class Employee {
    /**
     * Unique identifier for the employee.
     */
    public int id;

    /**
     * First name of the employee.
     */
    public String firstName;

    /**
     * Last name of the employee.
     */
    public String lastName;

    /**
     * Salary of the employee.
     */
    public double salary;

    /**
     * ID of the manager to whom this employee reports. Null if the employee has no manager.
     */
    public Integer managerId;

    /**
     * Constructs an Employee object with the given details.
     *
     * @param id         Unique identifier for the employee.
     * @param firstName  First name of the employee.
     * @param lastName   Last name of the employee.
     * @param salary     Salary of the employee.
     * @param managerId  ID of the manager to whom the employee reports. Can be null.
     */
    public Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getSalary() {
        return salary;
    }

    public Integer getManagerId() {
        return managerId;
    }

}
