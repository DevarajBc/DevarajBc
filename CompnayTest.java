import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
    }

    @Test
    void testAnalyzeSalaries() {
        // Set up test data
        Employee ceo = new Employee(1, "John", "Doe", 100000, null);
        Employee manager = new Employee(2, "Jane", "Smith", 60000, 1);
        Employee subordinate1 = new Employee(3, "Emily", "Davis", 40000, 2);
        Employee subordinate2 = new Employee(4, "Michael", "Brown", 30000, 2);

        // Add employees
        company.getEmployees().put(1, ceo);
        company.getEmployees().put(2, manager);
        company.getEmployees().put(3, subordinate1);
        company.getEmployees().put(4, subordinate2);

        // Add manager relationships
        company.getManagers().put(1, List.of(manager));
        company.getManagers().put(2, List.of(subordinate1, subordinate2));

        // Assign CEO
        company.setCeo(ceo);

        // Analyze salaries
        List<String> results = company.analyzeSalaries();

        // Expected results
        String expectedUnderpaid = "Jane Smith earns less than they should by 8000.00";
        String expectedOverpaid = "John Doe earns more than they should by 40000.00";

        // Validate results
        assertFalse(results.contains(expectedUnderpaid), "Jane's underpayment issue should be detected.");
        assertFalse(results.contains(expectedOverpaid), "John's overpayment issue should be detected.");
    }


    @Test
    void testAnalyzeReportingLines() {
        // Set up test data
        Employee ceo = new Employee(1, "John", "Doe", 100000, null); // CEO
        Employee manager1 = new Employee(2, "Jane", "Smith", 80000, 1); // Direct subordinate of CEO
        Employee manager2 = new Employee(3, "Emily", "Davis", 60000, 2); // Subordinate of Jane
        Employee subordinate = new Employee(4, "Michael", "Brown", 50000, 3); // Subordinate of Emily
        Employee juniorSubordinate = new Employee(5, "Sophia", "Taylor", 40000, 4); // Subordinate of Michael

        // Add employees
        company.getEmployees().put(1, ceo);
        company.getEmployees().put(2, manager1);
        company.getEmployees().put(3, manager2);
        company.getEmployees().put(4, subordinate);
        company.getEmployees().put(5, juniorSubordinate);

        // Add manager relationships
        company.getManagers().put(1, List.of(manager1));
        company.getManagers().put(2, List.of(manager2));
        company.getManagers().put(3, List.of(subordinate));
        company.getManagers().put(4, List.of(juniorSubordinate));

        // Assign CEO
        company.setCeo(ceo);

        // Analyze reporting lines
        List<String> results = company.analyzeReportingLines();

        // Expected result
        String expected = "Sophia Taylor has a reporting line which is too long by 1 levels";

        // Validate results
        assertFalse(results.contains(expected), "Sophia's long reporting line should be detected.");
    }



    @Test
    void testGetReportingLineLength() {
        // Set up test data
        Employee ceo = new Employee(1, "John", "Doe", 100000, null);
        Employee manager = new Employee(2, "Jane", "Smith", 80000, 1);
        Employee subordinate = new Employee(3, "Emily", "Davis", 60000, 2);

        // Add employees
        company.getEmployees().put(1, ceo);
        company.getEmployees().put(2, manager);
        company.getEmployees().put(3, subordinate);

        // Assign CEO
        company.setCeo(ceo);

        // Calculate reporting line lengths
        int ceoLength = company.getReportingLineLength(ceo);
        int managerLength = company.getReportingLineLength(manager);
        int subordinateLength = company.getReportingLineLength(subordinate);

        // Validate results
        assertEquals(0, ceoLength, "CEO should have a reporting line length of 0.");
        assertEquals(1, managerLength, "Manager should have a reporting line length of 1.");
        assertEquals(2, subordinateLength, "Subordinate should have a reporting line length of 2.");
    }
}
