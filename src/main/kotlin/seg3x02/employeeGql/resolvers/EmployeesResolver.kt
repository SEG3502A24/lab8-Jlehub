package seg3x02.employeeGql.resolvers

import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import java.util.*
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.resolvers.types.CreateEmployeeInput
import seg3x02.employeeGql.repository.EmployeesRepository

@Controller
class EmployeesResolver(
    private val employeeRepository: EmployeeRepository,
    private val mongoOperations: MongoOperations
) {

   
    @QueryMapping
    fun listEmployees(): List<Employee> {
        return employeeRepository.findAll()
    }

    
    @QueryMapping
    fun getEmployee(@Argument id: String): Employee? {
        return employeeRepository.findById(id).orElse(null)
    }

    
    @MutationMapping
    fun addEmployee(@Argument("createEmployeeInput") input: CreateEmployeeInput): Employee {
        if (input.name != null && input.dateOfBirth != null && input.city != null && input.salary != null) {
            val employee = Employee(
                id = UUID.randomUUID().toString(),
                name = input.name,
                dateOfBirth = input.dateOfBirth,
                city = input.city,
                salary = input.salary,
                gender = input.gender,
                email = input.email
            )
            employeeRepository.save(employee)
            return employee
        } else {
            throw IllegalArgumentException("Invalid input")
        }
    }

    
    @MutationMapping
    fun updateEmployee(
        @Argument id: String,
        @Argument("createEmployeeInput") input: CreateEmployeeInput
    ): Employee {
        val employee = employeeRepository.findById(id).orElseThrow { Exception("Employee not found") }
        
        val updatedEmployee = employee.copy(
            name = input.name ?: employee.name,
            dateOfBirth = input.dateOfBirth ?: employee.dateOfBirth,
            city = input.city ?: employee.city,
            salary = input.salary ?: employee.salary,
            gender = input.gender ?: employee.gender,
            email = input.email ?: employee.email
        )
        
        employeeRepository.save(updatedEmployee)
        return updatedEmployee
    }

    
    @MutationMapping
    fun deleteEmployee(@Argument id: String): Boolean {
        return if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id)
            true
        } else {
            false
        }
    }

}
