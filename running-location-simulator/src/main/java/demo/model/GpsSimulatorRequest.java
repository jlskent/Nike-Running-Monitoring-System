package demo.model;


import lombok.Data;


//get request from API
//use models to consume data

@Data
public class GpsSimulatorRequest {
    private String runningId;
    private double speed;
    private boolean move = true;
//    share location
    private boolean exportPositionToMessaging = true;
    private Integer reportInterval = 500;
    private RunnerStatus runnerStatus = RunnerStatus.NONE;
    private String polyline;
    private MedicalInfo medicalInfo;


}
