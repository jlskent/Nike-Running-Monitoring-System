package demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.model.SimulatorInitLocations;
import demo.service.PathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class DefaultPathService implements PathService {

    //jackson tool to process stream
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public SimulatorInitLocations loadSimulatorInitLocations() {

        //process stream
        final InputStream is = this.getClass().getResourceAsStream("/init-locations.json");
        try {
            return objectMapper.readValue(is, SimulatorInitLocations.class);
        }
        catch (IOException e){
            throw new IllegalStateException(e);
        }
    }
}













