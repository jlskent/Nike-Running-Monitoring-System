package demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@EnableBinding(Source.class)
public class RunnerPositionSource {

    @Autowired
    private MessageChannel output;


    //take request from running location simulator -> feed to rabbitMQ
    @RequestMapping(path = "api/locations", method = RequestMethod.POST)
    public void locations(@RequestBody String positionInfo) {
        log.info("Receiving current position info from simulator " + positionInfo);
        //create a channel, and put it in chanel

        this.output.send(MessageBuilder.withPayload(positionInfo).build());

    }
}
















