package br.com.dea.management.project;

import br.com.dea.management.employee.EmployeeTestUtils;
import br.com.dea.management.employee.EmployeeType;
import br.com.dea.management.employee.domain.Employee;
import br.com.dea.management.employee.repository.EmployeeRepository;
import br.com.dea.management.position.domain.Position;
import br.com.dea.management.position.repository.PositionRepository;
import br.com.dea.management.project.domain.Project;
import br.com.dea.management.project.repository.ProjectRepository;
import br.com.dea.management.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ProjectTestUtils {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeTestUtils employeeTestUtils;

    public void createFakeProject(int amount) {

        this.employeeTestUtils.createFakeEmployees(2);
        List<Employee> employeeList = this.employeeRepository.findAll();

        for (int i = 0; i < amount; i++) {
            Project project = new Project();
            project.setName("name " + i);
            project.setClient("client " + i);
            project.setExternalProductManager("manager " + i);
            project.setScrumMaster(employeeList.get(0));
            project.setProductOwner(employeeList.get(1));
            project.setStartDate(LocalDate.now());
            project.setEndDate(LocalDate.now());

            this.projectRepository.save(project);

        }
    }

}
