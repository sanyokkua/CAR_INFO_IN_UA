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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationDepartment.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationDepartment.NAME)})
public class RegistrationDepartment implements Serializable {

    @Id
    @Column(name = Constants.RegistrationDepartment.CODE, nullable = false)
    private Long departmentCode;

    @NaturalId
    @Column(name = Constants.RegistrationDepartment.NAME, nullable = false)
    private String departmentName;

    @Column(name = Constants.RegistrationDepartment.ADDRESS, nullable = false)
    private String departmentAddress;

    @Column(name = Constants.RegistrationDepartment.EMAIL, nullable = false)
    private String departmentEmail;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationDepartment", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
