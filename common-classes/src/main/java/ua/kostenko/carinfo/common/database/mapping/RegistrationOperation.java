package ua.kostenko.carinfo.common.database.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationOperation.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationOperation.NAME)})
class RegistrationOperation implements Serializable {

    @Id
    @Column(name = Constants.RegistrationOperation.CODE, nullable = false)//non NULLABLE
    private Long operationCode;

    @NaturalId
    @Column(name = Constants.RegistrationOperation.NAME, nullable = false)//non NULLABLE
    private String operationName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationOperation", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
