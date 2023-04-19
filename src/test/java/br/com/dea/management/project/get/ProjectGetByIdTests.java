package br.com.dea.management.project.get;

import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.project.ProjectTestUtils;
import br.com.dea.management.project.domain.Project;
import br.com.dea.management.project.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class ProjectGetByIdTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AcademyClassRepository academyClassRepository;

    @Autowired
    private ProjectTestUtils projectTestUtils;

    @BeforeEach
    void beforeEach() {
        log.info("Before each test in " + ProjectGetByIdTests.class.getSimpleName());
    }

    @BeforeAll
    void beforeSuiteTest() {
        log.info("Before all tests in " + ProjectGetByIdTests.class.getSimpleName());
    }

    @Test
    void whenRequestingAnExistentProjectById_thenReturnTheProjectSuccessfully() throws Exception {
        this.academyClassRepository.deleteAll();
        this.projectRepository.deleteAll();
        this.employeeRepository.deleteAll();

        this.projectTestUtils.createFakeProject(1);

        Project project = this.projectRepository.findAll().get(0);

        mockMvc.perform(get("/project/" + project.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.endDate").exists())
                .andExpect(jsonPath("$.name", is("name 0")))
                .andExpect(jsonPath("$.client", is("client 0")))
                .andExpect(jsonPath("$.externalProductManager", is("manager 0")))
                .andExpect(jsonPath("$.productOwner.name", is("name 1")))
                .andExpect(jsonPath("$.scrumMaster.name", is("name 0")));

    }

    @Test
    void whenRequestingByIdAndIdIsNotANumber_thenReturnTheBadRequestError() throws Exception {

        mockMvc.perform(get("/project/xx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void whenRequestingAnNonExistentProjectById_thenReturnTheNotFoundError() throws Exception {

        mockMvc.perform(get("/project/5000"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));

    }

}
