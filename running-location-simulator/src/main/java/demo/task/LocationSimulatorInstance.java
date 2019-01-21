package demo.task;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.Future;

//getter,setter
@Data
//constructor
@AllArgsConstructor


//wrap thread: thread -> instance obj
public class LocationSimulatorInstance {

    private long instanceId;
    private LocationSimulator locationSimulator;

    private Future<?> locationSimulatorTask;
}
