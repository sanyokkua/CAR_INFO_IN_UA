package ua.kostenko.carinfo.common.database.mapping;

import lombok.*;
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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationModel.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationModel.NAME)})
class RegistrationModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationModel.ID, nullable = false, columnDefinition = "serial")
    private Long modelId;

    @Column(name = Constants.RegistrationModel.NAME, nullable = false)
    private String modelName;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "registrationModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RegistrationVehicle> registrationVehicles = new HashSet<>();
}
