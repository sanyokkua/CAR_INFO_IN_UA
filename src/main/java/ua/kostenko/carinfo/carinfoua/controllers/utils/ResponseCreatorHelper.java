package ua.kostenko.carinfo.carinfoua.controllers.utils;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.AdministrativeObjectEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegionCodeEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.ServiceCenterEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.AdministrativeObjectsCrudRepository;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.RegionCodeCrudRepository;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.ServiceCenterCrudRepository;
import ua.kostenko.carinfo.carinfoua.data.presentation.Auto;
import ua.kostenko.carinfo.carinfoua.data.presentation.CombinedInformation;
import ua.kostenko.carinfo.carinfoua.data.presentation.Registration;
import ua.kostenko.carinfo.carinfoua.data.presentation.ServiceCenter;

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

    public List<CombinedInformation> getCombinedInformation(List<RegistrationInformationEntity> registrationInformationEntityList) {
        return registrationInformationEntityList.stream().map(registrationInformation -> {
            log.info("Mapping RegistrationInformationEntity to CombinedInformation from object: {}", registrationInformationEntityList);
            Auto auto = getAuto(registrationInformation);
            log.info("Created Auto object: {}", auto);
            AdministrativeObjectEntity administrativeObject = administrativeObjectsCrudRepository.findById(registrationInformation.getAdministrativeObjectCode()).orElse(null);
            log.info("Found AdministrativeObjectEntity {}", administrativeObject);
            Registration registration = getRegistration(registrationInformation, administrativeObject);
            log.info("Created Registration object: {}", registration);
            ServiceCenterEntity serviceCenterEntity = serviceCenterCrudRepository.findById(registrationInformation.getDepartmentCode()).orElse(null);
            log.info("Found ServiceCenterEntity object: {}", serviceCenterEntity);
            ServiceCenter serviceCenter = getServiceCenter(registrationInformation, serviceCenterEntity);
            log.info("Create ServiceCenter object: {}", serviceCenter);
            return new CombinedInformation(auto, registration, serviceCenter);
        }).collect(Collectors.toList());
    }

    private Auto getAuto(RegistrationInformationEntity registrationInformation) {
        return new Auto(registrationInformation.getCarBrand(), registrationInformation.getCarModel(), registrationInformation.getCarMakeYear(), registrationInformation.getCarColor(),
                registrationInformation.getCarKind(), registrationInformation.getCarBody(), registrationInformation.getCarPurpose(), registrationInformation.getCarFuel(),
                registrationInformation.getCarEngineCapacity(), registrationInformation.getCarOwnWeight(), registrationInformation.getCarTotalWeight());
    }

    private Registration getRegistration(RegistrationInformationEntity registrationInformation, AdministrativeObjectEntity administrativeObject) {
        String code = registrationInformation.getCarNewRegistrationNumber().substring(0, 2);
        RegionCodeEntity defaultIfEmpty = new RegionCodeEntity("", "");
        return new Registration(Registration.PersonKind.getPersonKind(registrationInformation.getPerson()), administrativeObject.getTypeName(), administrativeObject.getName(),
                registrationInformation.getOperationName(), registrationInformation.getRegistrationDate(), registrationInformation
                .getCarNewRegistrationNumber(),
                regionCodeCrudRepository.findById(code).orElse(defaultIfEmpty));
    }

    private ServiceCenter getServiceCenter(RegistrationInformationEntity registrationInformation, ServiceCenterEntity serviceCenterEntity) {
        return new ServiceCenter(serviceCenterEntity.getDepId(), serviceCenterEntity.getAddress(), serviceCenterEntity.getEmail(), registrationInformation
                .getDepartmentName());
    }
}
