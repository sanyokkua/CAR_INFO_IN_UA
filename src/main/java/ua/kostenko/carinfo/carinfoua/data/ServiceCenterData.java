package ua.kostenko.carinfo.carinfoua.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCenterData {
  @Id
  @Column(name = "dep_code", nullable = false)
  Long depId;
  @Column(name = "address")
  String address;
  @Column(name = "email")
  String email;

  enum ServiceCenterDataFields {
    DEP_ID("dep_code"),
    ADDRESS("address"),
    EMAIL("email");
    private String fieldName;

    ServiceCenterDataFields(String fieldName) {
      this.fieldName = fieldName;
    }

    public String getFieldName() {
      return fieldName;
    }
  }
}
