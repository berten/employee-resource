package be.collide.employees;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@RegisterForReflection

public class Employee {
    public static final String EMPLOYEE_ID_COLUMN = "id";
    public static final String EMPLOYEE_NAME_COLUMN = "name";
    public static final String EMPLOYEE_COMPANY_COLUMN = "company";
    private String id;
    private String name;
    private String company;

    public Employee(String id, String name, String company) {
        this.id = id;
        this.name = name;
        this.company = company;
    }

    public Employee() {
    }

    public static Employee from(Map<String, AttributeValue> item) {
        if (null != item && !item.isEmpty()) {
            return new Employee(item.get(EMPLOYEE_ID_COLUMN).s(), item.get(EMPLOYEE_NAME_COLUMN).s(), item.get(EMPLOYEE_COMPANY_COLUMN).s());
        } else
            return new Employee();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
