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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationKind.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationKind.NAME)})
class RegistrationKind implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationKind.ID, nullable = false, columnDefinition = "serial")
    private Long kindId;

    @NaturalId
    @Column(name = Constants.RegistrationKind.NAME, nullable = false)
    private String kindName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationKind", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
