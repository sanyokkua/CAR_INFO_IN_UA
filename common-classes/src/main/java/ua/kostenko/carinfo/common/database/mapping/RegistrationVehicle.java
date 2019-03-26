package ua.kostenko.carinfo.common.database.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationVehicle.TABLE)
public class RegistrationVehicle implements Serializable {

    @EmbeddedId
    private RegistrationVehicleId vehicleId;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationVehicle.VEHICLE_BRAND_ID)
    private RegistrationBrand registrationBrand;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationVehicle.VEHICLE_MODEL_ID)
    private RegistrationModel registrationModel;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationVehicle", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
