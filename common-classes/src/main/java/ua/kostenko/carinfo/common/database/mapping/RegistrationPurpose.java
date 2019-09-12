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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationPurpose.TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationPurpose.NAME)})
class RegistrationPurpose implements Serializable {

    private static final long serialVersionUID = 5579958424559803299L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationPurpose.ID, nullable = false, columnDefinition = "serial")
    private Long purposeId;

    @NaturalId
    @Column(name = Constants.RegistrationPurpose.NAME, nullable = false)
    private String purposeName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationPurpose", orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
