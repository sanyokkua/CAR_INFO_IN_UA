package ua.kostenko.carinfo.carinfoua.data;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "KOATUU")
public class KOATUU {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private Long idNumber;
  @Column(name = "typeName")
  private String typeName;
  @Column(name = "name")
  private String name;

  public KOATUU(long idNumber, String typeName, String name) {
    this.idNumber = idNumber;
    this.typeName = typeName;
    this.name = name;
  }

  public KOATUU() {
  }

  public enum KOATUUFields {
    ID_NUMBER("TE"),
    TYPE_NAME("NP"),
    NAME("NU");

    private String fieldName;

    KOATUUFields(String fieldName) {
      this.fieldName = fieldName;
    }

    public String getFieldName() {
      return fieldName;
    }
  }
}
