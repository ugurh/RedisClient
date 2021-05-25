package io.hrnugr.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class GenUnit implements Serializable {

    @Id
    private String unitId;
    private String unitName;
    private String unitType;
    private Date workingDate;
}
