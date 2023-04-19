package br.com.dea.management.academyclass.update;

import br.com.dea.management.AcademyTestUtils;
import br.com.dea.management.academyclass.ClassType;
import br.com.dea.management.academyclass.domain.AcademyClass;
import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.project.repository.ProjectRepository;
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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClassUpdatePayloadValidationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AcademyClassRepository academyClassRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AcademyTestUtils academyClassTestUtils;

    @Autowired
    private EmployeeRepository employeeRepository;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    void whenPayloadHasRequiredFieldsMissing_thenReturn400AndTheErrors() throws Exception {
        String payload = "{}";
        mockMvc.perform(put("/academy-class/1")
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(4)))
                .andExpect(jsonPath("$.details[*].field", hasItem("startDate")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("Start Date could not be null")))
                .andExpect(jsonPath("$.details[*].field", hasItem("endDate")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("End Date could not be null")))
                .andExpect(jsonPath("$.details[*].field", hasItem("classType")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("Class Type could not be null")))
                .andExpect(jsonPath("$.details[*].field", hasItem("instructorId")))
                .andExpect(jsonPath("$.details[*].errorMessage", hasItem("Instructor Id could not be null")));
    }

    @Test
    void whenEditingAnAcademyClassThatDoesNotExists_thenReturn404() throws Exception {
        this.academyClassRepository.deleteAll();

        String payload = "{" +
                "\"startDate\": \"2022-01-01\"," +
                "\"endDate\": \"2024-01-01\"," +
                "\"classType\": \"DESIGN\"," +
                "\"instructorId\": 1" +
                "}";
        mockMvc.perform(put("/academy-class/1")
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void whenEditingAnAcademyClassWithAnEmployeeThatDoesNotExists_thenReturn404() throws Exception {
        this.projectRepository.deleteAll();
        this.academyClassRepository.deleteAll();
        this.employeeRepository.deleteAll();
        this.academyClassTestUtils.createFakeClass(1, LocalDate.now(), LocalDate.now(), ClassType.DEVELOPER);

        AcademyClass academyClass = this.academyClassRepository.findAll().get(0);


        String payload = "{" +
                "\"startDate\": \"2022-01-01\"," +
                "\"endDate\": \"2024-01-01\"," +
                "\"classType\": \"DESIGN\"," +
                "\"instructorId\": 1000" +
                "}";
        mockMvc.perform(put("/academy-class/" + academyClass.getId())
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

}
