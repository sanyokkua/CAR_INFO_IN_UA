package ua.kostenko.carinfo.rest.controllers;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.kostenko.carinfo.rest.data.persistent.services.SearchService;
import ua.kostenko.carinfo.rest.data.presentation.CombinedInformation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class MainRestController {
    private final SearchService searchService;

    @Autowired
    public MainRestController(SearchService searchService) {
        Preconditions.checkNotNull(searchService);
        this.searchService = searchService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/registrations/{number}")
    public List<CombinedInformation> searchByNumber(@PathVariable String number) {
        number = number.toUpperCase();
        log.info("Processing request by url /search/{}", number);
        LocalDateTime before = LocalDateTime.now();
        List<CombinedInformation> combinedInformation = searchService.searchAllByRegistrationNumber(number.toUpperCase());
        log.info("Processing of request by url /search/{} finished, time: {} ms", number, Duration.between(before, LocalDateTime.now()).toMillis());
        return combinedInformation;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/brands")
    public List<String> getAllBrands() {
        return searchService.getAllBrands();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/brands/{brand}")
    public List<String> getAllModelsForBrand(@PathVariable String brand) {
        return searchService.getAllModelsForBrand(brand);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/colors")
    public List<String> getAllColors() {
        return searchService.getAllColors();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/fuel")
    public List<String> getAllFuelTypes() {
        return searchService.getAllFuelTypes();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/kinds")
    public List<String> getAllCarKinds() {
        return searchService.getAllCarKinds();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/regions")
    public List<String> getAllRegions() {
        return searchService.getAllRegions();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/registrations")
    public Map<String, Object> countAllRegistrations() {
        return buildResponseMap("registrations", searchService.countAllRegistrations());
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/countAllForBrand")
    public Map<String, Object> countAllByBrand(@RequestParam(value = "brand", defaultValue = "") String brand) {
        return buildResponseMap("countAllForBrand", searchService.countAllByBrand(brand));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/countAllForBrandAndModel")
    public Map<String, Object> countAllByCarBrandAndModel(@RequestParam(value = "brand") String brand, @RequestParam(value = "model") String model) {
        return buildResponseMap("countAllForBrandAndModel", searchService.countAllByCarBrandAndModel(brand, model));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/countAllForColor")
    public Map<String, Object> countAllByCarColor(@RequestParam(value = "color") String color) {
        return buildResponseMap("countAllForColor", searchService.countAllByCarColor(color));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/countAllForFuel")
    public Map<String, Object> countAllByFuelType(@RequestParam(value = "fuelType") String fuelType) {
        return buildResponseMap("countAllForFuel", searchService.countAllByFuelType(fuelType));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/countAllForKind")
    public Map<String, Object> countAllByCarKind(String carKind) {
        return buildResponseMap("countAllForKind", searchService.countAllByCarKind(carKind));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/countAllForYear")
    public Map<String, Object> countAllCarsByYear(@RequestParam(value = "year", defaultValue = "2018") int year) {
        return buildResponseMap("countAllForYear", searchService.countAllCarsByYear(year));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(value = "/countAllCarForRegion")
    public Map<String, Object> countAllCarsInRegion(@RequestParam(value = "region") String region) {
        return buildResponseMap("countAllCarForRegion", searchService.countAllCarsInRegion(region));
    }

    private Map<String, Object> buildResponseMap(String key, Object value) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put(key, value);
        return responseMap;
    }
}
