package ua.kostenko.carinfo.common.database.mapping;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.kostenko.carinfo.common.database.Constants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationModel.TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationModel.NAME)})
class RegistrationModel implements Serializable {

    private static final long serialVersionUID = -1109763476484909341L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationModel.ID, nullable = false, columnDefinition = "serial")
    private Long modelId;

    @Column(name = Constants.RegistrationModel.NAME, nullable = false) // non NULLABLE
    private String modelName;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "registrationModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RegistrationVehicle> registrationVehicles = new HashSet<>();
}
