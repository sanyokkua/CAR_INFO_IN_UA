package ua.kostenko.carinfo.common.database.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import ua.kostenko.carinfo.common.database.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationModel.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationModel.MODEL_NAME)})
public class RegistrationModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationModel.MODEL_ID, nullable = false, columnDefinition = "serial")
    private Long modelId;

    @NaturalId
    @Column(name = Constants.RegistrationModel.MODEL_NAME, nullable = false)
    private String modelName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationModel", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationVehicle> registrationVehicles = new HashSet<>();
}
