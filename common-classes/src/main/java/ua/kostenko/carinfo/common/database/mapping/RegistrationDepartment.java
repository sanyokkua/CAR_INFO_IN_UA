package ua.kostenko.carinfo.common.database.mapping;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationDepartment.TABLE)
class RegistrationDepartment implements Serializable {

    private static final long serialVersionUID = 5451209020789388265L;

    @Id
    @Column(name = Constants.RegistrationDepartment.CODE, nullable = false) // non NULLABLE
    private Long departmentCode;

    @Column(name = Constants.RegistrationDepartment.ADDRESS) // NULLABLE
    private String departmentAddress;

    @Column(name = Constants.RegistrationDepartment.EMAIL) // NULLABLE
    private String departmentEmail;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationDepartment", orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
