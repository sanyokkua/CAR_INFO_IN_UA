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
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationColor.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationColor.NAME)})
class RegistrationColor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationColor.ID, nullable = false, columnDefinition = "serial")
    private Long colorId;

    @NaturalId
    @Column(name = Constants.RegistrationColor.NAME, nullable = false)
    private String colorName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "registrationColor", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
