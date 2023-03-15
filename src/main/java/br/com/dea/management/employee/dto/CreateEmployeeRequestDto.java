package br.com.dea.management.employee.dto;

import br.com.dea.management.employee.EmployeeType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateEmployeeRequestDto {

    @NotNull(message = "Name could not be null")
    private String name;

    @NotNull(message = "Email could not be null")
    private String email;

    private String linkedin;

    private EmployeeType employeeType;

    private Long position;

    @NotNull(message = "Password could not be null")
    private String password;

}
