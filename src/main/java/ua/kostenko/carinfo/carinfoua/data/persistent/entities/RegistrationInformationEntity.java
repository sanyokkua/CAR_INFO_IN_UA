package ua.kostenko.carinfo.carinfoua.data.persistent.entities;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RegistrationInformation")
public class RegistrationInformationEntity { //Number of records 8 653 790
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "person")
    private String person; // e.g. P // P (person) or J (juridical)
    @Column(name = "reg_addr_koatuu")
    private Long administrativeObjectCode; // e.g. 1210436900
    @Column(name = "oper_code")
    private Long operationCode; // e.g. 440
    @Column(name = "oper_name")
    private String operationName; // e.g. 440 - ПЕРЕРЕЄСТРАЦIЯ ПРИ ВТРАТІ СВIДОЦТВА ПРО РЕЄСТРАЦIЮ
    @Column(name = "d_reg")
    private String registrationDate; // date of operation  yyy-mm-dd 2018-01-01
    @Column(name = "dep_code")
    private Long departmentCode; // number 1244
    @Column(name = "dep")
    private String departmentName; // TSC - number ТСЦ 1244
    @Column(name = "brand")
    private String carBrand; // e.g. Mitsubishi
    @Column(name = "model")
    private String carModel; // e.g. Lancer X
    @Column(name = "make_year")
    private Long carMakeYear; // date of creation current car 2007
    @Column(name = "color")
    private String carColor; // e.g. БІЛИЙ
    @Column(name = "kind")
    private String carKind; // e.g. ЛЕГКОВИЙ
    @Column(name = "body")
    private String carBody; // e.g. СЕДАН-B
    @Column(name = "purpose")
    private String carPurpose; // e.g. ЗАГАЛЬНИЙ
    @Column(name = "fuel")
    private String carFuel; // e.g. БЕНЗИН
    @Column(name = "capacity")
    private Long carEngineCapacity; // e.g. 1198
    @Column(name = "own_weight")
    private Long carOwnWeight; // e.g. 955
    @Column(name = "total_weight")
    private Long carTotalWeight; // e.g. 1355
    @Column(name = "n_reg_new")
    private String carNewRegistrationNumber; // AX3333EX
    @Column(name = "dataSetYear")
    private String dataSetYear;

    public static String createId(RegistrationInformationEntity object) {
        String stringBuilder = object.getPerson() + object.getRegistrationDate() + object.getDepartmentName() + object.getCarBrand() + object.getCarModel() + object.getCarColor() + object.getCarMakeYear()
                + object.getCarBody() + object.getCarNewRegistrationNumber() + object.getCarOwnWeight() + object.getCarTotalWeight() + object.getCarEngineCapacity() + object.getCarFuel() + object.getCarPurpose();
        return DigestUtils.sha512Hex(stringBuilder);
    }

    public boolean hasNullMainFields() {
        return isBlank(registrationDate) || isBlank(carBrand) || isBlank(carKind) || isBlank(carNewRegistrationNumber);
    }

    public enum RegistrationInformationEntityFields {
        PERSON("person"),
        ADMINISTRATIVE_OBJECT("reg_addr_koatuu"),
        OPERATION_CODE("oper_code"),
        OPERATION_NAME("oper_name"),
        REGISTRATION_DATE("d_reg"),
        DEPARTMENT_CODE("dep_code"),
        DEPARTMENT_NAME("dep"),
        CAR_BRAND("brand"),
        CAR_MODEL("model"),
        CAR_MAKE_YEAR("make_year"),
        CAR_COLOR("color"),
        CAR_KIND("kind"),
        CAR_BODY("body"),
        CAR_PURPOSE("purpose"),
        CAR_FUEL("fuel"),
        CAR_ENGINE_CAPACITY("capacity"),
        CAR_OWN_WEIGHT("own_weight"),
        CAR_TOTAL_WEIGHT("total_weight"),
        CAR_NEW_REGISTRATION_NUMBER("n_reg_new"),
        DATA_SET_YEAR("dataSetYear");

        private final String fieldName;

        RegistrationInformationEntityFields(String fieldName) {
            this.fieldName = fieldName;
        }

        public static RegistrationInformationEntityFields getInfoDataFieldByName(String fieldName) {
            Preconditions.checkState(isNotBlank(fieldName));
            return Stream.of(RegistrationInformationEntityFields.values()).filter(infoDataFields -> fieldName.equalsIgnoreCase(infoDataFields.getFieldName())).findFirst().get();
        }

        public String getFieldName() {
            return fieldName;
        }
    }

    public static class RegistrationInformationEntityBuilder {
        RegistrationInformationEntity entity;

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

        public RegistrationInformationEntity build() {
            if (entity.hasNullMainFields()) {
                return null;
            }
            return entity;
        }
    }
}
