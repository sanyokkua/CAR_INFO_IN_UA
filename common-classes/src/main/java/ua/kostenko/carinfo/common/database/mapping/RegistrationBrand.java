package ua.kostenko.carinfo.common.database.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationBrand.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationBrand.NAME)})
class RegistrationBrand implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationBrand.ID, nullable = false, columnDefinition = "serial")
    private Long brandId;

    @Column(name = Constants.RegistrationBrand.NAME, nullable = false)//non NULLABLE
    private String brandName;

    @Builder.Default
    @OneToMany(mappedBy = "registrationBrand", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RegistrationVehicle> registrationVehicles = new HashSet<>();
}
