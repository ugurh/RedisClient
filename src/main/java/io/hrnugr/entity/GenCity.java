package io.hrnugr.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class GenCity implements Serializable {

    @Id
    private String id;
    private String name;
    private String code;

}
