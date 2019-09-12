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
import lombok.NoArgsConstructor;
import ua.kostenko.carinfo.common.database.Constants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationBrand.TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationBrand.NAME)})
class RegistrationBrand implements Serializable {

    private static final long serialVersionUID = 773791125111198751L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationBrand.ID, nullable = false, columnDefinition = "serial")
    private Long brandId;

    @Column(name = Constants.RegistrationBrand.NAME, nullable = false) // non NULLABLE
    private String brandName;

    @Builder.Default
    @OneToMany(mappedBy = "registrationBrand", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RegistrationVehicle> registrationVehicles = new HashSet<>();
}
