package br.com.dea.management.academyclass.update;

import br.com.dea.management.AcademyTestUtils;
import br.com.dea.management.academyclass.ClassType;
import br.com.dea.management.academyclass.domain.AcademyClass;
import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.employee.EmployeeTestUtils;
import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClassUpdateSuccessCaseTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AcademyClassRepository academyClassRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeTestUtils employeeTestUtils;

    @Autowired
    private AcademyTestUtils academyClassTestUtils;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    void whenRequestingAcademyClassUpdateWithAValidPayload_thenUpdateAAcademyClassSuccessfully() throws Exception {
        this.academyClassRepository.deleteAll();
        this.employeeRepository.deleteAll();

        LocalDate startDate = LocalDate.of(2022, 01, 01);
        LocalDate endDate = startDate.plusYears(2);

        this.academyClassTestUtils.createFakeClass(1, startDate, endDate, ClassType.DEVELOPER);
        this.employeeTestUtils.createFakeEmployees(1);

        Employee employee = this.employeeRepository.findAll().get(0);
        AcademyClass academyClassBase = this.academyClassRepository.findAll().get(0);

        String payload = "{" +
                "\"startDate\": \"2022-01-01\"," +
                "\"endDate\": \"2024-01-01\"," +
                "\"classType\": \"DESIGN\"," +
                "\"instructorId\": " + employee.getId() +
                "}";
        mockMvc.perform(put("/academy-class/" + academyClassBase.getId())
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isOk());

        AcademyClass academyClass = this.academyClassRepository.findAll().get(0);

        assertThat(academyClass.getStartDate()).isEqualTo("2022-01-01");
        assertThat(academyClass.getEndDate()).isEqualTo("2024-01-01");
        assertThat(academyClass.getClassType().name()).isEqualTo("DESIGN");
        assertThat(academyClass.getInstructor().getId()).isEqualTo(employee.getId());
    }

}
