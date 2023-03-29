package br.com.dea.management.academyclass.dto;

import br.com.dea.management.academyclass.ClassType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateAcademyClassRequestDto {

    @NotNull(message = "Start Date could not be null")
    private LocalDate startDate;

    @NotNull(message = "End Date could not be null")
    private LocalDate endDate;

    @NotNull(message = "Class Type could not be null")
    private ClassType classType;

    @NotNull(message = "Instructor Id could not be null")
    private Long instructorId;

}
