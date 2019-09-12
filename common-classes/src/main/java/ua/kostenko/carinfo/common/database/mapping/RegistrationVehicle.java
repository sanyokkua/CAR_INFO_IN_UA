package ua.kostenko.carinfo.common.database.mapping;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationVehicle.TABLE,
        indexes = {
                @Index(columnList = Constants.RegistrationVehicle.MODEL_ID,
                        name = Constants.RegistrationVehicle.MODEL_ID),
                @Index(columnList = Constants.RegistrationVehicle.BRAND_ID,
                        name = Constants.RegistrationVehicle.BRAND_ID)})
class RegistrationVehicle implements Serializable {

    private static final long serialVersionUID = -31408076431720382L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationVehicle.ID, nullable = false, columnDefinition = "serial")
    private Long vehicleId;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationVehicle.BRAND_ID, nullable = false) // non NULLABLE
    private RegistrationBrand registrationBrand;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationVehicle.MODEL_ID, nullable = false) // non NULLABLE
    private RegistrationModel registrationModel;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationVehicle", orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();

}
