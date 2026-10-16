package ch.tbz.m450.testing.tools;

import ch.tbz.m450.testing.tools.controller.StudentController;
import ch.tbz.m450.testing.tools.repository.StudentRepository;
import ch.tbz.m450.testing.tools.repository.entities.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        reset(studentRepository);
    }

    @Test
    void validStudentIsSaved() throws Exception {
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Denis\",\"email\":\"denis@tbz.ch\"}"))
                .andExpect(status().isOk());

        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void emptyNameReturns400() throws Exception {
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"denis@tbz.ch\"}"))
                .andExpect(status().isBadRequest());

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void wrongEmailReturns400() throws Exception {
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Denis\",\"email\":\"keine-email\"}"))
                .andExpect(status().isBadRequest());

        verify(studentRepository, never()).save(any(Student.class));
    }
}
