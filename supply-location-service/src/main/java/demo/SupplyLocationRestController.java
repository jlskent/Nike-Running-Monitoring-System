package demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SupplyLocationRestController {

    //inject repo and service
    private SupplyLocationRepository repository;
    private SupplyLocationService service;
    @Autowired
    public SupplyLocationRestController(SupplyLocationRepository repository, SupplyLocationService service){
        this.repository = repository;
        this.service = service;
    }


//    upload to mongoDB
    @RequestMapping(value = "/bulk/supplyLocations", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void upload(@RequestBody List<SupplyLocation> locations) {
        this.repository.saveAll(locations);
    }

//    delete
    @RequestMapping(value = "/purge", method = RequestMethod.POST)
    public void delete() { this.repository.deleteAll(); }

    //for testing
    public List<SupplyLocation> uploadFilteredLocations(List<SupplyLocation> locations) {
        return this.service.saveSupplyLocationsZipContains503(locations);
    }
}
