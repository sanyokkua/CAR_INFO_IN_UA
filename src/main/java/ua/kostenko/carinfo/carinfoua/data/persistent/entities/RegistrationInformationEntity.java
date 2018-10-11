package ua.kostenko.carinfo.carinfoua.data.persistent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RegistrationInformation")
public class RegistrationInformationEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "person")
    private String person;
    @Column(name = "reg_addr_koatuu")
    private Long administrativeObjectCode;
    @Column(name = "oper_code")
    private Long operationCode;
    @Column(name = "oper_name")
    private String operationName;
    @Column(name = "d_reg")
    private String registrationDate;
    @Column(name = "dep_code")
    private Long departmentCode;
    @Column(name = "dep")
    private String departmentName;
    @Column(name = "brand")
    private String carBrand;
    @Column(name = "model")
    private String carModel;
    @Column(name = "make_year")
    private Long carMakeYear;
    @Column(name = "color")
    private String carColor;
    @Column(name = "kind")
    private String carKind;
    @Column(name = "body")
    private String carBody;
    @Column(name = "purpose")
    private String carPurpose;
    @Column(name = "fuel")
    private String carFuel;
    @Column(name = "capacity")
    private Long carEngineCapacity;
    @Column(name = "own_weight")
    private Long carOwnWeight;
    @Column(name = "total_weight")
    private Long carTotalWeight;
    @Column(name = "n_reg_new")
    private String carNewRegistrationNumber;
    @Column(name = "dataSetYear")
    private String dataSetYear;

    public static String createId(RegistrationInformationEntity object) {
        return UUID.nameUUIDFromBytes(object.concatAllFields().getBytes()).toString();
    }

    private String concatAllFields() {
        return person + administrativeObjectCode + operationCode + operationName + registrationDate + departmentCode + departmentName + carBrand + carModel + carMakeYear + carColor + carKind + carBody + carPurpose + carFuel + carEngineCapacity + carOwnWeight + carTotalWeight + carNewRegistrationNumber;
    }

    public static class RegistrationInformationEntityBuilder {
        final RegistrationInformationEntity entity;

        public RegistrationInformationEntityBuilder() {
            entity = new RegistrationInformationEntity();
        }

        private String getValueOrEmptyString(String value) {
            if (isBlank(value)) {
                return "";
            }
            return value;
        }

        public RegistrationInformationEntityBuilder setId(String id) {
            entity.setId(getValueOrEmptyString(id));
            return this;
        }

        public RegistrationInformationEntityBuilder setPerson(String person) {
            entity.setPerson(getValueOrEmptyString(person));
            return this;
        }

        public RegistrationInformationEntityBuilder setAdministrativeObjectCode(Long administrativeObjectCode) {
            entity.setAdministrativeObjectCode(administrativeObjectCode);
            return this;
        }

        public RegistrationInformationEntityBuilder setOperationCode(Long operationCode) {
            entity.setOperationCode(operationCode);
            return this;
        }

        public RegistrationInformationEntityBuilder setOperationName(String operationName) {
            entity.setOperationName(getValueOrEmptyString(operationName));
            return this;
        }

        public RegistrationInformationEntityBuilder setRegistrationDate(String registrationDate) {
            entity.setRegistrationDate(getValueOrEmptyString(registrationDate));
            return this;
        }

        public RegistrationInformationEntityBuilder setDepartmentCode(Long departmentCode) {
            entity.setDepartmentCode(departmentCode);
            return this;
        }

        public RegistrationInformationEntityBuilder setDepartmentName(String departmentName) {
            entity.setDepartmentName(getValueOrEmptyString(departmentName));
            return this;
        }

        public RegistrationInformationEntityBuilder setCarBrand(String carBrand) {
            entity.setCarBrand(getValueOrEmptyString(carBrand));
            return this;
        }

        public RegistrationInformationEntityBuilder setCarModel(String carModel) {
            entity.setCarModel(getValueOrEmptyString(carModel));
            return this;
        }

        public RegistrationInformationEntityBuilder setCarMakeYear(Long carMakeYear) {
            entity.setCarMakeYear(carMakeYear);
            return this;
        }

        public RegistrationInformationEntityBuilder setCarColor(String carColor) {
            entity.setCarColor(getValueOrEmptyString(carColor));
            return this;
        }

        public RegistrationInformationEntityBuilder setCarKind(String carKind) {
            entity.setCarKind(getValueOrEmptyString(carKind));
            return this;
        }

        public RegistrationInformationEntityBuilder setCarBody(String carBody) {
            entity.setCarBody(getValueOrEmptyString(carBody));
            return this;
        }

        public RegistrationInformationEntityBuilder setCarPurpose(String carPurpose) {
            entity.setCarPurpose(getValueOrEmptyString(carPurpose));
            return this;
        }

        public RegistrationInformationEntityBuilder setCarFuel(String carFuel) {
            entity.setCarFuel(getValueOrEmptyString(carFuel));
            return this;
        }

        public RegistrationInformationEntityBuilder setCarEngineCapacity(Long carEngineCapacity) {
            entity.setCarEngineCapacity(carEngineCapacity);
            return this;
        }

        public RegistrationInformationEntityBuilder setCarOwnWeight(Long carOwnWeight) {
            entity.setCarOwnWeight(carOwnWeight);
            return this;
        }

        public RegistrationInformationEntityBuilder setCarTotalWeight(Long carTotalWeight) {
            entity.setCarTotalWeight(carTotalWeight);
            return this;
        }

        public RegistrationInformationEntityBuilder setCarNewRegistrationNumber(String carNewRegistrationNumber) {
            entity.setCarNewRegistrationNumber(getValueOrEmptyString(carNewRegistrationNumber));
            return this;
        }

        public RegistrationInformationEntityBuilder setDataSetYear(String dataSetYear) {
            entity.setDataSetYear(getValueOrEmptyString(dataSetYear));
            return this;
        }

        private boolean hasNullMainFields() {
            return isBlank(entity.getRegistrationDate()) || isBlank(entity.getCarBrand()) || isBlank(entity.getCarKind());
        }

        public RegistrationInformationEntity build() {
            if (hasNullMainFields()) {
                return null;
            }
            return entity;
        }
    }
}
