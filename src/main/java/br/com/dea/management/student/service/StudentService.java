package br.com.dea.management.student.service;

import br.com.dea.management.exceptions.NotFoundException;
import br.com.dea.management.student.domain.Student;
import br.com.dea.management.student.dto.CreateStudentRequestDto;
import br.com.dea.management.student.dto.UpdateStudentRequestDto;
import br.com.dea.management.student.repository.StudentRepository;
import br.com.dea.management.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> findAllStudents() {
        return this.studentRepository.findAll();
    }

    public Page<Student> findAllStudentsPaginated(Integer page, Integer pageSize) {
        return this.studentRepository.findAllPaginated(PageRequest.of(page, pageSize, Sort.by("user.name").ascending()));
    }

    public Student findStudentById(Long id) {
        return this.studentRepository.findById(id).orElseThrow(() -> new NotFoundException(Student.class, id));
    }

    public Student createStudent(CreateStudentRequestDto createStudentRequestDto) {
        User user = User.builder()
                .name(createStudentRequestDto.getName())
                .email(createStudentRequestDto.getEmail())
                .password(createStudentRequestDto.getPassword())
                .linkedin(createStudentRequestDto.getLinkedin())
                .build();

        Student student = Student.builder()
                .user(user)
                .finishDate(createStudentRequestDto.getFinishDate())
                .graduation(createStudentRequestDto.getGraduation())
                .university(createStudentRequestDto.getUniversity())
                .build();

        return this.studentRepository.save(student);
    }

    public Student updateStudent(Long studentId, UpdateStudentRequestDto updateStudentRequestDto) {
        this.findStudentById(studentId);

        User user = User.builder()
                .name(updateStudentRequestDto.getName())
                .email(updateStudentRequestDto.getEmail())
                .password(updateStudentRequestDto.getPassword())
                .linkedin(updateStudentRequestDto.getLinkedin())
                .build();

        Student student = Student.builder()
                .user(user)
                .finishDate(updateStudentRequestDto.getFinishDate())
                .graduation(updateStudentRequestDto.getGraduation())
                .university(updateStudentRequestDto.getUniversity())
                .build();

        return this.studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        Student student = this.findStudentById(studentId);
        this.studentRepository.delete(student);
    }

}
