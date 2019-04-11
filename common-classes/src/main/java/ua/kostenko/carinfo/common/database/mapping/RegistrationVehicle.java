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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationVehicle.TABLE)
class RegistrationVehicle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationVehicle.ID, nullable = false, columnDefinition = "serial")
    private Long vehicleId;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationVehicle.BRAND_ID, nullable = false)//non NULLABLE
    private RegistrationBrand registrationBrand;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationVehicle.MODEL_ID, nullable = false)//non NULLABLE
    private RegistrationModel registrationModel;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationVehicle", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();

}
