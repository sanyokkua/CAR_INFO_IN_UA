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
@Table(name = "RegionCode")
public class RegionCodeEntity {
    @Id
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "region")
    private String region;
}
