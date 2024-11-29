import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Analyzes the organizational structure of a company and provides reports on salary compliance
 * and reporting line lengths.
 */
public class Company {

    /**
     * Map of employee ID to Employee object.
     */
    private final Map<Integer, Employee> employees = new HashMap<>();
    /**
     * Map of manager ID to a list of their direct subordinates.
     */
    private final Map<Integer, List<Employee>> managers = new HashMap<>();
    /**
     * The CEO of the company. Null if not set.
     */
    private Employee ceo;

    /**
     * Loads employee data from a CSV file.
     *
     * @param fileName Path to the CSV file containing employee data.
     * @throws IOException If an error occurs while reading the file.
     */
    public void loadEmployees(String fileName) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(Company.class.getClassLoader().getResource(fileName).toURI()))) {
            Iterator<String> iterator = lines.iterator();
            if (!iterator.hasNext()) {
                throw new IOException("File is empty");
            }

            // Read the header line and map column names to indexes
            String[] headers = iterator.next().split(",");
            Map<String, Integer> columnIndexMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                columnIndexMap.put(headers[i].trim(), i);
            }

            // Process each subsequent line
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] parts = line.split(",");

                try {
                    int id = Integer.parseInt(parts[columnIndexMap.get("Id")]);
                    String firstName = parts[columnIndexMap.get("firstName")];
                    String lastName = parts[columnIndexMap.get("lastName")];
                    double salary = Double.parseDouble(parts[columnIndexMap.get("salary")]);
                    Integer managerId = parts[columnIndexMap.get("managerId")].isEmpty()
                            ? null
                            : Integer.parseInt(parts[columnIndexMap.get("managerId")]);

                    Employee employee = new Employee(id, firstName, lastName, salary, managerId);
                    employees.put(id, employee);

                    if (managerId != null) {
                        managers.computeIfAbsent(managerId, k -> new ArrayList<>()).add(employee);
                    } else {
                        ceo = employee;
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line + ". Skipping this line.");
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Analyzes manager salaries and reports which managers:
     * <ul>
     *     <li>Earn less than 20% above the average salary of their subordinates, and by how much.</li>
     *     <li>Earn more than 50% above the average salary of their subordinates, and by how much.</li>
     * </ul>
     */
    public List<String> analyzeSalaries() {
        List<String> results = new ArrayList<>();
        for (Employee manager : employees.values()) {
            List<Employee> subordinates = managers.get(manager.id);
            if (subordinates != null && !subordinates.isEmpty()) {
                double totalSalary = subordinates.stream().mapToDouble(sub -> sub.salary).sum();
                double averageSalary = totalSalary / subordinates.size();
                double minSalary = 1.2 * averageSalary;
                double maxSalary = 1.5 * averageSalary;

                if (manager.salary < minSalary) {
                    results.add(manager.firstName + " " + manager.lastName + " earns less than they should by " +
                            String.format("%.2f", (minSalary - manager.salary)));
                } else if (manager.salary > maxSalary) {
                    results.add(manager.firstName + " " + manager.lastName + " earns more than they should by " +
                            String.format("%.2f", (manager.salary - maxSalary)));
                }
            }
        }
        return results;
    }


    public List<String> analyzeReportingLines() {
        List<String> results = new ArrayList<>();
        for (Employee employee : employees.values()) {
            if (employee.managerId != null) {
                int reportingLineLength = getReportingLineLength(employee);
                if (reportingLineLength > 4) {
                    results.add(employee.firstName + " " + employee.lastName + " has a reporting line which is too long by " + (reportingLineLength - 4) + " levels");
                }
            }
        }
        return results;
    }

    /**
     * Calculates the length of the reporting line (number of levels) between an employee and the CEO.
     *
     * @param employee The employee whose reporting line length is to be calculated.
     * @return The length of the reporting line. Returns 0 if the employee is the CEO or if no manager is found.
     */
    int getReportingLineLength(Employee employee) {
        int length = 0;
        Employee current = employee;
        Set<Integer> visited = new HashSet<>(); // To prevent circular references

        while (current.managerId != null) {
            if (visited.contains(current.id)) {
                System.err.println("Circular reference detected for employee ID: " + current.id);
                break;
            }
            visited.add(current.id);
            current = employees.get(current.managerId);
            if (current == null) {
                break; // Handle missing managers gracefully
            }
            length++;
        }
        return length;
    }


    public Map<Integer, Employee> getEmployees() {
        return employees;
    }

    public Map<Integer, List<Employee>> getManagers() {
        return managers;
    }

    public void setCeo(Employee ceo) {
        this.ceo = ceo;
    }

    public Employee getCeo() {
        return ceo;
    }
}
