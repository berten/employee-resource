package be.collide.employees;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@RegisterForReflection
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Employee {
    public static final String EMPLOYEE_ID_COLUMN = "id";
    public static final String EMPLOYEE_NAME_COLUMN = "name";
    public static final String EMPLOYEE_COMPANY_COLUMN = "company";
    private String id;
    private String name;
    private String company;

    public static Employee from(Map<String, AttributeValue> item) {
        if (null != item && !item.isEmpty()) {
            return new Employee(item.get(EMPLOYEE_ID_COLUMN).s(), item.get(EMPLOYEE_NAME_COLUMN).s(), item.get(EMPLOYEE_COMPANY_COLUMN).s());
        } else
            return new Employee();
    }
}
