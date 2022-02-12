package be.collide.employees;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.collide.employees.Employee.*;

@ApplicationScoped
public class Employees {

    @Inject
    DynamoDbClient dynamoDB;

    public List<Employee> findAll() {
        return dynamoDB.scanPaginator(scanRequest()).items().stream()
                .map(Employee::from)
                .collect(Collectors.toList());
    }

    public List<Employee> add(Employee employee) {
        dynamoDB.putItem(putRequest(employee));
        return findAll();
    }

    public Employee get(String id) {
        Employee employee = from(dynamoDB.getItem(getRequest(id)).item());
        if(employee.getId() == null) throw new NotFoundException("Could not find Employee with id " + id);
        else return employee;
    }

    public void delete(String id) {
        dynamoDB.deleteItem(deleteRequest(id));
    }


    public String getTableName() {
        return "Employees";
    }

    protected ScanRequest scanRequest() {
        return ScanRequest.builder().tableName(getTableName())
                .attributesToGet(EMPLOYEE_ID_COLUMN, EMPLOYEE_NAME_COLUMN, EMPLOYEE_COMPANY_COLUMN).build();
    }

    protected PutItemRequest putRequest(Employee employee) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(EMPLOYEE_ID_COLUMN, AttributeValue.builder().s(employee.getId()).build());
        item.put(EMPLOYEE_NAME_COLUMN, AttributeValue.builder().s(employee.getName()).build());
        item.put(EMPLOYEE_COMPANY_COLUMN, AttributeValue.builder().s(employee.getCompany()).build());

        return PutItemRequest.builder()
                .tableName(getTableName())
                .item(item)
                .build();
    }

    protected GetItemRequest getRequest(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(EMPLOYEE_ID_COLUMN, AttributeValue.builder().s(id).build());

        return GetItemRequest.builder()
                .tableName(getTableName())
                .key(key)
                .attributesToGet(EMPLOYEE_ID_COLUMN, EMPLOYEE_NAME_COLUMN, EMPLOYEE_COMPANY_COLUMN)
                .build();
    }

    protected DeleteItemRequest deleteRequest(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(EMPLOYEE_ID_COLUMN, AttributeValue.builder().s(id).build());

        return DeleteItemRequest.builder()
                .tableName(getTableName())
                .key(key)
                .build();
    }
}
