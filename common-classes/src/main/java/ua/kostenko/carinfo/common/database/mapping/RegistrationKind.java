package ua.kostenko.carinfo.common.database.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import ua.kostenko.carinfo.common.database.Constants;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = Constants.SCHEMA, name = Constants.RegistrationKind.TABLE, uniqueConstraints = {@UniqueConstraint(columnNames = Constants.RegistrationKind.KIND_NAME)})
public class RegistrationKind implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constants.RegistrationKind.KIND_ID, nullable = false, columnDefinition = "serial")
    private Long kindId;

    @NaturalId
    @Column(name = Constants.RegistrationKind.KIND_NAME, nullable = false)
    private String kindName;
}
