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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationBrand.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationBrand.BRAND_NAME)})
public class RegistrationBrand implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationBrand.BRAND_ID, nullable = false, columnDefinition = "serial")
    private Long brandId;

    @NaturalId
    @Column(name = Constants.RegistrationBrand.BRAND_NAME, nullable = false)
    private String brandName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationBrand", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationVehicle> registrationVehicles = new HashSet<>();
}
