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
    @MapsId("brandId")
    private RegistrationBrand registrationBrand;

    @ManyToOne
    @MapsId("modelId")
    private RegistrationModel registrationModel;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationVehicle", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();

    public RegistrationVehicle(RegistrationBrand registrationBrand, RegistrationModel registrationModel) {
        this.registrationBrand = registrationBrand;
        this.registrationModel = registrationModel;
        this.vehicleId = RegistrationVehicleId.builder().brandId(registrationBrand.getBrandId()).modelId(registrationModel.getModelId()).build();
    }
}
