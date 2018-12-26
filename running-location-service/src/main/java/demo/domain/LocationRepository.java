package demo.domain;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;


/*extends jpa
param:
entity name : Location
id data type*/
@RepositoryRestResource(path = "locations")
public interface LocationRepository extends JpaRepository<Location, Long> {

    @RestResource(path = "runners")

//    db =  movementType
//    select * from Locations where RunnerMovementType = movementType
    Page<Location> findByRunnerMovementType(@Param("movementType") Location.RunnerMovementType movementType, Pageable pageable);
    Page<Location> findByUnitInfoRunningId(@Param("runningId") String runningId, Pageable pageable);


}
