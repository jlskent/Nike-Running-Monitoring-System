package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplyLocationServiceImpl implements SupplyLocationService {

    private SupplyLocationRepository repository;

    @Autowired
    public SupplyLocationServiceImpl(SupplyLocationRepository repository) {this.repository = repository;}

    @Override
    //save filtered
    public List<SupplyLocation> saveSupplyLocationsZipContains503(List<SupplyLocation> locations) {
        List<SupplyLocation> save = filterList(locations, "503");
        return (ArrayList<SupplyLocation>) this.repository.saveAll(save);
    }

    //put in list if contains keyword
    private List<SupplyLocation> filterList(List<SupplyLocation> listToFilter, String keyword){
        List<SupplyLocation> save = new ArrayList<>();
        for (SupplyLocation supplyLocation : listToFilter) {
            if(supplyLocation.getZip().contains(keyword)) {
                save.add(supplyLocation);
            }
        }
        return save;
    }
}
