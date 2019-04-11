package ua.kostenko.carinfo.common.database.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import ua.kostenko.carinfo.common.api.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationFuelType.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationFuelType.NAME)})
class RegistrationFuelType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationFuelType.ID, nullable = false, columnDefinition = "serial")
    private Long fuelTypeId;

    @NaturalId
    @Column(name = Constants.RegistrationFuelType.NAME, nullable = false)
    private String fuelTypeName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationFuelType", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
