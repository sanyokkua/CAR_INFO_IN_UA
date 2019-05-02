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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationRecord.TABLE,
        indexes = {@Index(columnList = Constants.RegistrationRecord.REGISTRATION_NUMBER, name = Constants.RegistrationRecord.REGISTRATION_NUMBER),
                   @Index(columnList = Constants.RegistrationRecord.REGISTRATION_DATE, name = Constants.RegistrationRecord.REGISTRATION_DATE)})
class RegistrationRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationRecord.ID, nullable = false, columnDefinition = "serial")
    private Long registrationId;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.ADMIN_OBJ_ID)//NULLABLE
    private AdministrativeObject administrativeObject;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.OPERATION_CODE, nullable = false)//non NULLABLE
    private RegistrationOperation registrationOperation;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.DEPARTMENT_CODE, nullable = false)//non NULLABLE
    private RegistrationDepartment registrationDepartment;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.KIND, nullable = false)//non NULLABLE
    private RegistrationKind registrationKind;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.VEHICLE_ID, nullable = false)//non NULLABLE
    private RegistrationVehicle registrationVehicle;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.COLOR_ID, nullable = false)//non NULLABLE
    private RegistrationColor registrationColor;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.BODY_TYPE_ID)//NULLABLE
    private RegistrationBodyType registrationBodyType;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.PURPOSE_ID, nullable = false)//non NULLABLE
    private RegistrationPurpose registrationPurpose;

    @ManyToOne
    @JoinColumn(name = Constants.RegistrationRecord.FUEL_TYPE_ID)//NULLABLE
    private RegistrationFuelType registrationFuelType;

    @Column(name = Constants.RegistrationRecord.ENGINE_CAPACITY)//NULLABLE
    private Long engineCapacity;

    @Column(name = Constants.RegistrationRecord.OWN_WEIGHT)//NULLABLE
    private Long ownWeight;

    @Column(name = Constants.RegistrationRecord.TOTAL_WEIGHT)//NULLABLE
    private Long totalWeight;

    @Column(name = Constants.RegistrationRecord.MAKE_YEAR, nullable = false)//non NULLABLE
    private Long makeYear;

    @Column(name = Constants.RegistrationRecord.REGISTRATION_NUMBER) //NULLABLE
    private String registrationNumber;

    @Column(name = Constants.RegistrationRecord.REGISTRATION_DATE, nullable = false)//non NULLABLE
    private Date registrationDate;

    @Column(name = Constants.RegistrationRecord.PERSON_TYPE, nullable = false)//non NULLABLE
    private String personType;
}
