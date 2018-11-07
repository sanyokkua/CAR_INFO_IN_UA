package ua.kostenko.carinfo.importing.data.entities;

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
public class AdministrativeObjectEntity {
    private Long id;
    private String typeName;
    private String name;
}
