package br.com.dea.management.project.repository;

import br.com.dea.management.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p")
    @EntityGraph(attributePaths = {"productOwner", "scrumMaster"})
    public Page<Project> findAllPaginated(Pageable pageable);

}
