package io.hrnugr.dto;

import lombok.*;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GenUnitDto {

    @Id
    @NotNull
    @Size(min = 4, max = 4)
    private String unitId;
    private String unitName;
    private String unitType;
    private Date workingDate;

}
