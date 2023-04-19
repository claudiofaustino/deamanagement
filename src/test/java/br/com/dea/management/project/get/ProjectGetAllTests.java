package br.com.dea.management.project.get;

import br.com.dea.management.AcademyTestUtils;
import br.com.dea.management.academyclass.ClassType;
import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.project.ProjectTestUtils;
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

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class ProjectGetAllTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectTestUtils projectTestUtils;

    @Autowired
    private AcademyClassRepository academyClassRepository;

    @BeforeEach
    void beforeEach() {
        log.info("Before each test in " + ProjectGetAllTests.class.getSimpleName());
    }

    @BeforeAll
    void beforeSuiteTest() {
        log.info("Before all tests in " + ProjectGetAllTests.class.getSimpleName());
    }

    @Test
    void whenRequestingProjectList_thenReturnListOfProjectPaginatedSuccessfully() throws Exception {
        this.academyClassRepository.deleteAll();
        this.projectRepository.deleteAll();
        this.employeeRepository.deleteAll();

        this.projectTestUtils.createFakeProject(9);

        mockMvc.perform(get("/project?page=0&pageSize=2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].startDate").exists())
                .andExpect(jsonPath("$.content[0].endDate").exists())
                .andExpect(jsonPath("$.content[0].name", is("name 0")))
                .andExpect(jsonPath("$.content[0].client", is("client 0")))
                .andExpect(jsonPath("$.content[0].externalProductManager", is("manager 0")))
                .andExpect(jsonPath("$.content[0].productOwner.name", is("name 1")))
                .andExpect(jsonPath("$.content[0].scrumMaster.name", is("name 0")));

    }

    @Test
    void whenRequestingProjectListAndPageQueryParamIsInvalid_thenReturnBadRequestError() throws Exception {
        mockMvc.perform(get("/project?page=xx&pageSize=4"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void whenRequestingProjectListAndPageQueryParamIsMissing_thenReturnBadRequestError() throws Exception {
        mockMvc.perform(get("/project?pageSize=4"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void whenRequestingProjectListAndPageSizeQueryParamIsInvalid_thenReturnBadRequestError() throws Exception {
        mockMvc.perform(get("/project?pageSize=xx&page=4"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }

    @Test
    void whenRequestingProjectListAndPageSizeQueryParamIsMissing_thenReturnBadRequestError() throws Exception {
        mockMvc.perform(get("/project?page=0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details", hasSize(1)));
    }
    
}
