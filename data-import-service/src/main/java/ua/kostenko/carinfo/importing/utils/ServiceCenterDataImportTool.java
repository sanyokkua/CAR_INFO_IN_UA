package ua.kostenko.carinfo.importing.utils;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.ServiceCenterEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.ServiceCenterCrudRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@Slf4j
public class ServiceCenterDataImportTool implements Initializer {
    private final ApplicationProperties applicationProperties;
    private final ServiceCenterCrudRepository serviceCenterCrudRepository;

    @Autowired
    public ServiceCenterDataImportTool(ApplicationProperties applicationProperties, ServiceCenterCrudRepository serviceCenterCrudRepository) {
        Preconditions.checkNotNull(applicationProperties);
        Preconditions.checkNotNull(serviceCenterCrudRepository);
        this.applicationProperties = applicationProperties;
        this.serviceCenterCrudRepository = serviceCenterCrudRepository;
    }

    @Override
    public void init() {
        log.info("Starting initialization of ServiceCenters data.");
        Document htmlPageDocument = null;
        try {
            log.info("Opening page by url {}", applicationProperties.APP_DATA_SERVICE_CENTER_URL);
            htmlPageDocument = Jsoup.connect(applicationProperties.APP_DATA_SERVICE_CENTER_URL).get();
        } catch (IOException e) {
            log.error(String.format("Error with parsing or connecting to url: %s", applicationProperties.APP_DATA_SERVICE_CENTER_URL), e);
        }
        if (Objects.nonNull(htmlPageDocument)) {
            log.info("Parsing all information from page {}", applicationProperties.APP_DATA_SERVICE_CENTER_URL);
            List<ServiceCenterEntity> resultList = new ArrayList<>();
            Elements tbody = htmlPageDocument.select(applicationProperties.APP_DATA_SERVICE_CENTER_CSS_SELECTOR);
            for (Element tr : tbody) {
                List<Element> childTd = tr.childNodes()
                        .stream()
                        .filter(node -> node instanceof Element)
                        .map(node -> ((Element) node))
                        .filter(element -> isNotBlank(element.text()))
                        .collect(Collectors.toList());
                if (childTd.size() > 4) {
                    String departmentId = childTd.get(0).text().trim();
                    String address = childTd.get(1).text().trim();
                    String email = childTd.get(4).text().trim();
                    ServiceCenterEntity serviceCenterEntity = new ServiceCenterEntity(Long.valueOf(departmentId), address, email);
                    resultList.add(serviceCenterEntity);
                }
            }
            resultList.forEach(
                    serviceCenterEntity -> log.info("Service center: id {}, address {}, email {}", serviceCenterEntity.getDepId(), serviceCenterEntity.getAddress(), serviceCenterEntity.getEmail()));
            serviceCenterCrudRepository.saveAll(resultList);
            log.info("All information parsed");
        }
        log.info("Finished initialization of ServiceCenters data.");
    }

}
