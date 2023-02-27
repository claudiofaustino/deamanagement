package br.com.dea.management.student.update;

import br.com.dea.management.student.StudentTestUtils;
import br.com.dea.management.student.domain.Student;
import br.com.dea.management.student.repository.StudentRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentUpdateSuccessCaseTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentTestUtils studentTestUtils;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    void whenRequestingStudentUpdateWithAValidPayload_thenUpdateAStudentSuccessfully() throws Exception {
        this.studentRepository.deleteAll();
        this.studentTestUtils.createFakeStudents(1);

        Student studentBase = this.studentRepository.findAll().get(0);

        String payload = "{" +
                "\"name\": \"name\"," +
                "\"email\": \"email\"," +
                "\"linkedin\": \"linkedin\"," +
                "\"university\": \"university\"," +
                "\"graduation\": \"graduation\"," +
                "\"password\": \"password\"," +
                "\"finishDate\": \"2023-02-27\"" +
                "}";
        mockMvc.perform(put("/student/" + studentBase.getId())
                        .contentType(APPLICATION_JSON_UTF8).content(payload))
                .andExpect(status().isOk());

        Student student = this.studentRepository.findAll().get(0);

        assertThat(student.getUser().getName()).isEqualTo("name 0");
        assertThat(student.getUser().getEmail()).isEqualTo("email 0");
        assertThat(student.getUser().getLinkedin()).isEqualTo("linkedin 0");
        assertThat(student.getUser().getPassword()).isEqualTo("password 0");
        assertThat(student.getGraduation()).isEqualTo("graduation 0");
        assertThat(student.getUniversity()).isEqualTo("university 0");
    }

}
