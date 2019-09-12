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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationKind.TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationKind.NAME)})
class RegistrationKind implements Serializable {

    private static final long serialVersionUID = -157204954220956068L;

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
