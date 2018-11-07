package ua.kostenko.carinfo.consuming.data.persistent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AdministrativeObject")
public class AdministrativeObjectEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "typeName")
    private String typeName;
    @Column(name = "name")
    private String name;
}
