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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationPurpose.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationPurpose.NAME)})
public class RegistrationPurpose implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationPurpose.ID, nullable = false, columnDefinition = "serial")
    private Long purposeId;

    @NaturalId
    @Column(name = Constants.RegistrationPurpose.NAME, nullable = false)
    private String purposeName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationPurpose", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
