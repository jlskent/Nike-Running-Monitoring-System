package demo.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


// JSON pass from front to back -> Object model < -ORM-> Relational Model and save to DB


//JSON (un)serialzation
@JsonInclude(JsonInclude.Include.NON_NULL)


//ORM
@Entity
@Table(name = "LOCATIONS")

//lombok
@Data
public class Location {

    enum GpsStatus{
        EXCELLENT, OK, UNRELIABLE, BAD, NOFIX, UNKNOWN
    }

    //N    default is protected
    public enum RunnerMovementType{
        STOPPED, IN_MOTION
    }

    @Id
    @GeneratedValue
    private long id;

//    flatten composite
    @Embedded
//    map field bandMake to col Unit_band_make in relational db
    @AttributeOverride(name = "bandMake", column = @Column(name = "Unit_band_make"))
    private UnitInfo unitInfo;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fmi", column = @Column(name = "medical_fmi")),
            @AttributeOverride(name = "bfr", column = @Column(name = "medical_bfr"))
    })
    private MedicalInfo medicalInfo;
    private double latitude;
    private double longitude;
    private String heading;
    private double gpsSpeed;
    private GpsStatus gpsStatus;
    private double odometer;
    private double totalRunningTime;
    private double totalIdleTime;
    private double totalCalorieBurnt;
    private String address;
    private Date timestamp = new Date();
    private String gearProvider;
    private RunnerMovementType runnerMovementType;
    private String serviceType;



//    constructors
    public Location() {
        this.unitInfo = null;
    }


    @JsonCreator
//    map runnerId var to Json key
    public Location(@JsonProperty("runningId") String runningId) {
        this.unitInfo = new UnitInfo(runningId);
    }

    public Location(UnitInfo unitInfo) {
        this.unitInfo = unitInfo;
    }

    public String getRunningId() {
        return this.unitInfo == null ? null : this.unitInfo.getRunningId();
    }






    
}
