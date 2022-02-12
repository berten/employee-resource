package be.collide.employees;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeEmployeeResourceIT extends EmployeeResourceTest {

    // Execute the same tests but in native mode.
}