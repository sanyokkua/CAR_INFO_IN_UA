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
@Table(schema = Constants.SCHEMA, name = Constants.AdminObject.TABLE)
class AdministrativeObject implements Serializable {

    @Id
    @Column(name = Constants.AdminObject.ID, nullable = false)
    private Long adminObjId;

    @Column(name = Constants.AdminObject.TYPE)
    private String adminObjType;

    @NaturalId
    @Column(name = Constants.AdminObject.NAME, nullable = false)
    private String adminObjName;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "administrativeObject", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<RegistrationRecord> registrationRecords = new HashSet<>();
}
