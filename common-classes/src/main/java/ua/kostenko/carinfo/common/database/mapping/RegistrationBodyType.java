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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationBodyType.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationBodyType.NAME)})
class RegistrationBodyType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationBodyType.ID, nullable = false, columnDefinition = "serial")
    private Long bodyTypeId;

    @NaturalId
    @Column(name = Constants.RegistrationBodyType.NAME, nullable = false)
    private String bodyTypeName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationBodyType", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
