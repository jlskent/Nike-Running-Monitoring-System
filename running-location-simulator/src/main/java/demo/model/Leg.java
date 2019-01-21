package demo.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//if json has the same name
//lombok generate constructor of the class
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Leg {
    private int id;
    private Point startPosition;
    private Point endPosition;
    private double length;
    private double heading;

    public Leg() {
    }
}
