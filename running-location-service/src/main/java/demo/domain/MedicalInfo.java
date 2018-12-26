package demo.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import javax.persistence.Embeddable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Embeddable
@Data
public class MedicalInfo {

    private long bfr;
    private long fmi;

    public MedicalInfo() {
    }

    public MedicalInfo(long bfr, long fmi) {
        this.bfr = bfr;
        this.fmi = fmi;
    }
}
