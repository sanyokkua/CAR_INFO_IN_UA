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
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.NaturalId;
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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationOperation.TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationOperation.NAME)})
class RegistrationOperation implements Serializable {

    private static final long serialVersionUID = -5901882213533846417L;

    @Id
    @Column(name = Constants.RegistrationOperation.CODE, nullable = false) // non NULLABLE
    private Long operationCode;

    @NaturalId
    @Column(name = Constants.RegistrationOperation.NAME, nullable = false) // non NULLABLE
    private String operationName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationOperation", orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
