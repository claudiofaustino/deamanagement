package br.com.dea.management.project.controller;

import br.com.dea.management.project.domain.Project;
import br.com.dea.management.project.dto.CreateProjectRequestDto;
import br.com.dea.management.project.dto.ProjectDto;
import br.com.dea.management.project.dto.UpdateProjectRequestDto;
import br.com.dea.management.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Tag(name = "Project", description = "The Project API")
public class ProjectController {

    @Autowired
    ProjectService ProjectService;

    @Operation(summary = "Loads the list of Project paginated.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Page or Page Size params not valid"),
            @ApiResponse(responseCode = "500", description = "Error fetching Project list"),
    })
    @GetMapping("/project")
    public Page<ProjectDto> getProject(@RequestParam(required = true) Integer page,
                                       @RequestParam(required = true) Integer pageSize) {

        log.info(String.format("Fetching Project : page : %s : pageSize", page, pageSize));

        Page<Project> ProjectPaged = this.ProjectService.findAllProjectPaginated(page, pageSize);
        Page<ProjectDto> Projects = ProjectPaged.map(Project -> ProjectDto.fromProject(Project));

        log.info(String.format("Project loaded successfully : Project : %s : pageSize", Projects.getContent().size()));

        return Projects;

    }

    @Operation(summary = "Loads the Project by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Project Id invalid"),
            @ApiResponse(responseCode = "404", description = "Project Not found"),
            @ApiResponse(responseCode = "500", description = "Error fetching Project list"),
    })
    @GetMapping("/project/{id}")
    public ProjectDto getProject(@PathVariable Long id) {

        log.info(String.format("Fetching Project by id : Id : %s", id));

        ProjectDto Project = ProjectDto.fromProject(this.ProjectService.findProjectById(id));

        log.info(String.format("Project loaded successfully : Project : %s", Project));

        return Project;

    }

    @Operation(summary = "Creates a new Project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Payload not valid"),
            @ApiResponse(responseCode = "500", description = "Error creating project"),
    })
    @PostMapping("/project")
    public void createClass(@Valid @RequestBody CreateProjectRequestDto createProjectRequestDto) {
        log.info(String.format("Creating Project : Payload : %s", createProjectRequestDto));

        Project Project = this.ProjectService.createProject(createProjectRequestDto);

        log.info(String.format("Project created successfully : id : %s", Project.getId()));
    }

    @Operation(summary = "Updates a Project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Payload not valid"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Error updating Project"),
    })
    @PutMapping("/project/{id}")
    public void updateEmployee(@PathVariable Long id, @Valid @RequestBody UpdateProjectRequestDto updateProjectRequestDto) {
        log.info(String.format("Updating Class : Payload : %s", updateProjectRequestDto));

        Project Project = this.ProjectService.updateClass(id, updateProjectRequestDto);

        log.info(String.format("Class updated successfully : id : %s", Project.getId()));
    }

}
