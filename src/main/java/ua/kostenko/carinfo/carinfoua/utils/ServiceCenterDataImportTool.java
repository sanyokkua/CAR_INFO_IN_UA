package ua.kostenko.carinfo.carinfoua.utils;

import com.google.common.base.Preconditions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ServiceCenterDataImportTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCenterDataImportTool.class);
    private final ApplicationProperties applicationProperties;
    private final ServiceCenterCrudRepository serviceCenterCrudRepository;

    @Autowired
    public ServiceCenterDataImportTool(ApplicationProperties applicationProperties, ServiceCenterCrudRepository serviceCenterCrudRepository) {
        Preconditions.checkNotNull(applicationProperties);
        Preconditions.checkNotNull(serviceCenterCrudRepository);
        this.applicationProperties = applicationProperties;
        this.serviceCenterCrudRepository = serviceCenterCrudRepository;
    }

    public void initDB() {
        LOGGER.info("Starting initialization of ServiceCenters data.");
        Document htmlPageDocument = null;
        try {
            LOGGER.info("Opening page by url {}", applicationProperties.APP_DATA_SERVICE_CENTER_URL);
            htmlPageDocument = Jsoup.connect(applicationProperties.APP_DATA_SERVICE_CENTER_URL).get();
        } catch (IOException e) {
            LOGGER.error(String.format("Error with parsing or connecting to url: %s", applicationProperties.APP_DATA_SERVICE_CENTER_URL), e);
        }
        if (Objects.nonNull(htmlPageDocument)) {
            LOGGER.info("Parsing all information from page {}", applicationProperties.APP_DATA_SERVICE_CENTER_URL);
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
                    serviceCenterEntity -> LOGGER.info("Service center: id {}, address {}, email {}", serviceCenterEntity.getDepId(), serviceCenterEntity.getAddress(), serviceCenterEntity.getEmail()));
            serviceCenterCrudRepository.saveAll(resultList);
            LOGGER.info("All information parsed");
        }
        LOGGER.info("Finished initialization of ServiceCenters data.");
    }

}
