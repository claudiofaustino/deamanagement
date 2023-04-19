package br.com.dea.management.academyclass.service;

import br.com.dea.management.academyclass.domain.AcademyClass;
import br.com.dea.management.academyclass.dto.CreateAcademyClassRequestDto;
import br.com.dea.management.academyclass.dto.UpdateAcademyClassRequestDto;
import br.com.dea.management.academyclass.repository.AcademyClassRepository;
import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AcademyClassService {

    @Autowired
    private AcademyClassRepository academyClassRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Page<AcademyClass> findAllAcademyClassPaginated(Integer page, Integer pageSize) {
        return this.academyClassRepository.findAllPaginated(PageRequest.of(page, pageSize, Sort.by("startDate").ascending()));
    }

    public AcademyClass findAcademyClassById(Long id) {
        return this.academyClassRepository.findById(id).orElseThrow(() -> new NotFoundException(AcademyClass.class, id));
    }

    public AcademyClass createClass(CreateAcademyClassRequestDto createAcademyClassRequestDto) {
        Employee employee = this.employeeRepository.findById(createAcademyClassRequestDto.getInstructorId())
                .orElseThrow(() -> new NotFoundException(Employee.class, createAcademyClassRequestDto.getInstructorId()));

        AcademyClass academyClass = AcademyClass.builder()
                .classType(createAcademyClassRequestDto.getClassType())
                .startDate(createAcademyClassRequestDto.getStartDate())
                .endDate(createAcademyClassRequestDto.getEndDate())
                .instructor(employee)
                .build();

        return this.academyClassRepository.save(academyClass);
    }

    public AcademyClass updateClass(Long classId, UpdateAcademyClassRequestDto updateAcademyClassRequestDto) {
        AcademyClass academyClass = this.findAcademyClassById(classId);

        Employee employee = this.employeeRepository.findById(updateAcademyClassRequestDto.getInstructorId())
                .orElseThrow(() -> new NotFoundException(Employee.class, updateAcademyClassRequestDto.getInstructorId()));

        academyClass.setClassType(updateAcademyClassRequestDto.getClassType());
        academyClass.setEndDate(updateAcademyClassRequestDto.getEndDate());
        academyClass.setStartDate(updateAcademyClassRequestDto.getStartDate());
        academyClass.setInstructor(employee);

        return this.academyClassRepository.save(academyClass);
    }

}
