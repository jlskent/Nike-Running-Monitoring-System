package demo.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import demo.model.CurrentPosition;
import demo.service.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//lombok logger
@Slf4j
@Service
public class DefaultPositionService implements PositionService {

    //before sending request: log!!!
    //private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPositionService.class);
//    private RestTemplate restTemplate = new RestTemplate();

    @Autowired //by restTemplate from ribbon
    private RestTemplate restTemplate;

    //value from yml file
    @Value("${com.example.running.location.distribution}")
    private String runningLocationDistribution;


    @HystrixCommand(fallbackMethod = "processPositionInfoFallback")
    @Override
    public void processPositionInfo(long id, CurrentPosition currentPosition, boolean sendPositionToDistributionService) {
        //string: registered name in eureka in distribution
        String runningLocationDistribution = "http://running-location-distribution";

        if (sendPositionToDistributionService) {
            log.info(String.format("Thread %d Simulator is calling distribution rest api", Thread.currentThread().getId()));
            log.info("current location "+currentPosition);
            this.restTemplate.postForLocation(runningLocationDistribution + "/api/locations", currentPosition);
        }
    }

    //fallback function-for above one
    public void processPositionInfoFallback(long id, CurrentPosition currentPosition, boolean sendPositionToDistributionService) {
        log.error("HystrixFallback Method, Unable to send message for distribution");
        //TODO more error handling
    }

}












