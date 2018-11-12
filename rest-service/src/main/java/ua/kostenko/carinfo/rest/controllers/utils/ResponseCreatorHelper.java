package ua.kostenko.carinfo.rest.controllers.utils;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.entities.AdministrativeObjectEntity;
import ua.kostenko.carinfo.common.entities.RegionCodeEntity;
import ua.kostenko.carinfo.common.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.common.entities.ServiceCenterEntity;
import ua.kostenko.carinfo.rest.data.persistent.repositories.AdministrativeObjectsCrudRepository;
import ua.kostenko.carinfo.rest.data.persistent.repositories.RegionCodeCrudRepository;
import ua.kostenko.carinfo.rest.data.persistent.repositories.ServiceCenterCrudRepository;
import ua.kostenko.carinfo.rest.data.presentation.Auto;
import ua.kostenko.carinfo.rest.data.presentation.CombinedInformation;
import ua.kostenko.carinfo.rest.data.presentation.Registration;
import ua.kostenko.carinfo.rest.data.presentation.ServiceCenter;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ResponseCreatorHelper {
    private final AdministrativeObjectsCrudRepository administrativeObjectsCrudRepository;
    private final ServiceCenterCrudRepository serviceCenterCrudRepository;
    private final RegionCodeCrudRepository regionCodeCrudRepository;

    public ResponseCreatorHelper(AdministrativeObjectsCrudRepository administrativeObjectsCrudRepository, ServiceCenterCrudRepository serviceCenterCrudRepository,
                                 RegionCodeCrudRepository regionCodeCrudRepository) {
        Preconditions.checkNotNull(administrativeObjectsCrudRepository);
        Preconditions.checkNotNull(serviceCenterCrudRepository);
        Preconditions.checkNotNull(regionCodeCrudRepository);
        this.administrativeObjectsCrudRepository = administrativeObjectsCrudRepository;
        this.serviceCenterCrudRepository = serviceCenterCrudRepository;
        this.regionCodeCrudRepository = regionCodeCrudRepository;
    }
}
