package ua.kostenko.carinfo.carinfoua.data.persistent.entities;

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
@Table(name = "ServiceCenter")
public class ServiceCenterEntity {
  @Id
  @Column(name = "dep_code", nullable = false)
  Long depId;
  @Column(name = "address")
  String address;
  @Column(name = "email")
  String email;
}



