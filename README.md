Mockito is a popular Java framework used for creating mock objects, which help simulate the behavior of real objects in unit testing. Let's break down some key terminology in Mockito to get you started:

### Key Terms in Mockito:

1. **Mock**:  
   A mock is a simulated version of an object. In Mockito, you can create mocks using `Mockito.mock(ClassName.class)`.

2. **Stubbing**:  
   Stubbing is the process of pre-defining the behavior of a mock when a specific method is called. For example:
   ```java
   when(mockedObject.methodCall()).thenReturn(desiredValue);
   ```

3. **Spy**:  
   A spy is a partial mock—it lets you mock some methods while keeping the actual behavior of others. You can create spies using `Mockito.spy(ClassName)`.

4. **Verification**:  
   Verifying ensures that a specific method on a mock was called with the expected arguments. Example:
   ```java
   verify(mockedObject).methodCall();
   ```

5. **Argument Matchers**:  
   Matchers like `any()`, `eq()`, etc., are used when defining behavior or verifying interactions. For example:
   ```java
   when(mockedObject.methodCall(anyString())).thenReturn(desiredValue);
   ```

6. **Annotations**:  
   Mockito provides annotations like `@Mock`, `@InjectMocks`, and `@Spy` to make your code cleaner. These require the use of `MockitoAnnotations.initMocks()`.

7. **InOrder**:  
   Allows you to verify that methods were called in a specific order. Example:
   ```java
   InOrder inOrder = inOrder(mock1, mock2);
   inOrder.verify(mock1).firstMethod();
   inOrder.verify(mock2).secondMethod();
   ```

8. **doAnswer / doThrow / doNothing**:  
   Used to define behavior for void methods or more advanced scenarios. For example:
   ```java
   doThrow(new RuntimeException()).when(mockedObject).voidMethod();
   ```
To mock a service with a repository in Mockito, you can create unit tests where the repository is mocked and the service is tested in isolation. Here's how you can do this step by step:

### 1. **Service and Repository Code**
Let’s assume you have a `UserService` class that depends on a `UserRepository`.

**UserRepository** (interface):
```java
public interface UserRepository {
    Optional<User> findById(Long id);
    User save(User user);
}
```

**UserService**:
```java
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
```

### 2. **Unit Test with Mockito**

Here’s how you can write a test for the `UserService` by mocking the `UserRepository`:

```java
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUserById() {
        // Arrange: Prepare mock data
        User mockUser = new User(1L, "John Doe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act: Call the service method
        User result = userService.getUserById(1L);

        // Assert: Verify the result
        assertEquals("John Doe", result.getName());
        verify(userRepository).findById(1L);
    }

    @Test
    public void testSaveUser() {
        // Arrange: Prepare mock data
        User mockUser = new User(2L, "Jane Doe");
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // Act: Call the service method
        User result = userService.saveUser(mockUser);

        // Assert: Verify the result
        assertEquals("Jane Doe", result.getName());
        verify(userRepository).save(mockUser);
    }
}
```

### Key Annotations and Methods Used:
1. **`@Mock`**: Creates a mock instance of the repository.
2. **`@InjectMocks`**: Injects the mocked repository into the service being tested.
3. **`when(...).thenReturn(...)`**: Stubs the behavior of the repository.
4. **`verify(...)`**: Verifies that the repository method was called with the expected arguments.


### **Using `@Captor` in Mockito**
`@Captor` is used in Mockito to capture method arguments passed to a mock during execution. This is especially useful when you need to verify or inspect the actual arguments used in a method call.

---

### **🔹 Example Scenario**
Let’s say you have a `EmployeeService` that calls `EmployeeRepository.save(employee)`, and you want to verify what `Employee` object was passed to the `save` method.

---

### **✅ 1️⃣ Setup Classes**

#### **Employee Entity**
```java
public class Employee {
    private Long id;
    private String name;
    private String role;

    // Constructor, Getters, and Setters
    public Employee(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
}
```

#### **EmployeeRepository (Mocked)**
```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
```

#### **EmployeeService**
```java
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void saveEmployee(String name, String role) {
        Employee employee = new Employee(null, name, role);
        employeeRepository.save(employee);
    }
}
```

---

### **✅ 2️⃣ Write Unit Test with `@Captor`**
```java
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Captor
    private ArgumentCaptor<Employee> employeeCaptor; // Captor for Employee argument

    @Test
    void testSaveEmployee() {
        // Act: Call the method
        employeeService.saveEmployee("John Doe", "Developer");

        // Assert: Capture the argument passed to save()
        verify(employeeRepository).save(employeeCaptor.capture());

        Employee capturedEmployee = employeeCaptor.getValue(); // Get the captured value

        assertEquals("John Doe", capturedEmployee.getName());  // Verify captured value
        assertEquals("Developer", capturedEmployee.getRole());
        assertNull(capturedEmployee.getId());  // Ensure ID is null before saving
    }
}
```

---

### **✅ 3️⃣ Explanation**
1. **`@Captor private ArgumentCaptor<Employee> employeeCaptor;`**  
   - Captures the `Employee` object passed to `save()`.
   
2. **`verify(employeeRepository).save(employeeCaptor.capture());`**  
   - Ensures `save()` was called and captures its argument.
   
3. **`Employee capturedEmployee = employeeCaptor.getValue();`**  
   - Retrieves the actual object passed to `save()`.
   
4. **Assertions (`assertEquals(...)`)**  
   - Confirms that the `Employee` object had the expected properties.

---

### **🎯 When to Use `@Captor`?**
- When you need to **verify** or **inspect** arguments passed to a mock method.
- When the argument is **modified inside a method** and you need to check its final state.
- When using `verify(mock).method(argumentCaptor.capture())`.

---

### **🚀 Final Takeaway**
✅ `@Captor` helps you test **how a method modifies an object before passing it**.  
✅ **No need for manual `ArgumentCaptor.forClass(Employee.class)`** when using `@Captor`.  
✅ **Ensures method interactions are correct** without relying only on return values.



