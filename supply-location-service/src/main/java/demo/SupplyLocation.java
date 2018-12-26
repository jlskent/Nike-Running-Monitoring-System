package demo;

//use jackson to handle json
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
//for working with mongoDB
@Document
//lombok: all fileds's keyword will be maintained when creating java obj from db, eg final
@Data
@RequiredArgsConstructor(onConstructor = @__(@PersistenceConstructor))
public class SupplyLocation {

//    fileds
    @Id
    private String id;
    private String address1;
    private String address2;
    private String city;

//    mongoDB indexing
    @GeoSpatialIndexed
    @JsonIgnore
    private final Point location;
    private String state;
    private String zip;
    private String type;

//    default constructor
    public SupplyLocation() {
        this.location = new Point(0, 0);
    }


    @JsonCreator
    public SupplyLocation(@JsonProperty("longitude") double longitude, @JsonProperty("latitude") double latitude) {
        this.location = new Point(longitude, latitude);
    }


//    get log
    public double getLongitude() { return this.location.getX(); }

//    get lat
    public double getLatitude() { return this.location.getY(); }




    //@JsonProperty  Json -> java field
    //@JsonCreator  json unserialization
    //jackson err:
    //com.fasterxml.jackson.databind.JsonMappingException
    //Api viewer: Swagger/hal browser
}
