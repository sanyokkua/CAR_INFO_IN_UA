package ua.kostenko.carinfo.common.database.mapping;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kostenko.carinfo.common.database.Constants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

import static java.util.Objects.isNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class RegistrationVehicleId implements Serializable {

    @Column(name = Constants.RegistrationVehicle.BRAND_ID)
    private Long brandId;

    @Column(name = Constants.RegistrationVehicle.MODEL_ID)
    private Long modelId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (isNull(o) || getClass() != o.getClass()) {
            return false;
        }
        RegistrationVehicleId that = (RegistrationVehicleId) o;
        return Objects.equal(brandId, that.brandId) &&
                Objects.equal(modelId, that.modelId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(brandId, modelId);
    }
}
