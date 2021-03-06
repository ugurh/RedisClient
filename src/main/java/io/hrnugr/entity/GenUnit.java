package io.hrnugr.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull
    @Size(min = 4, max = 4)
    private String unitId;
    private String unitName;
    private String unitType;
    private Date workingDate;
}
