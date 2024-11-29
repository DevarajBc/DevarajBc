import java.io.IOException;

import java.util.List;

public class CsvReporter {

    public static void main(String[] args) throws IOException {
        // Load the CSV file from the resources directory
        Company company = new Company();
        company.loadEmployees("file.csv");
        List<String> salaryAnalysis = company.analyzeSalaries();
        List<String> reportingAnalysis = company.analyzeReportingLines();
        salaryAnalysis.forEach(System.out::println);
        reportingAnalysis.forEach(System.out::println);
    }
    }
