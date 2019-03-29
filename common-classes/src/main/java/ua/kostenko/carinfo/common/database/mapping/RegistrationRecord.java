package ua.kostenko.carinfo.common.database.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kostenko.carinfo.common.database.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationRecord.TABLE)
public class RegistrationRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationRecord.ID, nullable = false, columnDefinition = "serial")
    private Long registrationId;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.ADMIN_OBJ_ID)
    private AdministrativeObject administrativeObject;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.OPERATION_CODE)
    private RegistrationOperation registrationOperation;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.DEPARTMENT_CODE)
    private RegistrationDepartment registrationDepartment;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.KIND)
    private RegistrationKind registrationKind;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.VEHICLE_ID)
    private RegistrationVehicle registrationVehicle;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.COLOR_ID)
    private RegistrationColor registrationColor;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.BODY_TYPE_ID)
    private RegistrationBodyType registrationBodyType;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.PURPOSE_ID)
    private RegistrationPurpose registrationPurpose;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.FUEL_TYPE_ID)
    private RegistrationFuelType registrationFuelType;

    @Column(name = Constants.RegistrationRecord.ENGINE_CAPACITY)
    private Long engineCapacity;

    @Column(name = Constants.RegistrationRecord.OWN_WEIGHT)
    private Long ownWeight;

    @Column(name = Constants.RegistrationRecord.TOTAL_WEIGHT)
    private Long totalWeight;

    @Column(name = Constants.RegistrationRecord.MAKE_YEAR)
    private Long makeYear;

    @Column(name = Constants.RegistrationRecord.REGISTRATION_NUMBER)
    private String registrationNumber;

    @Column(name = Constants.RegistrationRecord.REGISTRATION_DATE)
    private Date registrationDate;
}
