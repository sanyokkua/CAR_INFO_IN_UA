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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationBodyType.TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationBodyType.NAME)})
class RegistrationBodyType implements Serializable {

    private static final long serialVersionUID = 4843213422556437129L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationBodyType.ID, nullable = false, columnDefinition = "serial")
    private Long bodyTypeId;

    @NaturalId
    @Column(name = Constants.RegistrationBodyType.NAME, nullable = false)
    private String bodyTypeName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationBodyType", orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
