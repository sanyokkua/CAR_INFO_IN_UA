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
@Table(schema = Constants.SCHEMA, name = Constants.AdminObject.TABLE)
class AdministrativeObject implements Serializable {

    private static final long serialVersionUID = 3078506134402972387L;

    @Id
    @Column(name = Constants.AdminObject.ID, nullable = false)
    private Long adminObjId;

    @Column(name = Constants.AdminObject.TYPE)
    private String adminObjType;

    @NaturalId
    @Column(name = Constants.AdminObject.NAME, nullable = false)
    private String adminObjName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administrativeObject", orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
