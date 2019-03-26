package ua.kostenko.carinfo.importing.importing.centers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.importing.configuration.ApplicationProperties;
import ua.kostenko.carinfo.importing.importing.Initializer;
import ua.kostenko.carinfo.importing.importing.Persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@Slf4j
public class ServiceCenterInitializer implements Initializer {
    private final ApplicationProperties applicationProperties;

    @Autowired
    public ServiceCenterInitializer(@NonNull ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void init() {
        log.info("init: Starting initialization of ServiceCenters data.");
        try {
            log.info("Opening page by url {}", applicationProperties.APP_DATA_SERVICE_CENTER_URL);
            Document htmlPageDocument = Jsoup.connect(applicationProperties.APP_DATA_SERVICE_CENTER_URL).get();
            if (Objects.nonNull(htmlPageDocument)) {
                log.info("init: Parsing  all information from page {}", applicationProperties.APP_DATA_SERVICE_CENTER_URL);
                List<ServiceCenter> resultList = new ArrayList<>();
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
                        ServiceCenter serviceCenterEntity = new ServiceCenter(Long.valueOf(departmentId), address, email);
                        resultList.add(serviceCenterEntity);
                    }
                }
                resultList.forEach(serviceCenterEntity -> log.info("init: Service center: id {}, address {}, email {}", serviceCenterEntity.getDepId(), serviceCenterEntity.getAddress(), serviceCenterEntity.getEmail()));
                Persist<ServiceCenter> persist = new ServiceCenterPersist();
                resultList.stream().parallel().forEach(persist::persist);
                log.info("init: All information parsed");
            }
            log.info("init: Finished initialization of ServiceCenters data.");
        } catch (IOException e) {
            log.error(String.format("init: Error with parsing or connecting to url: %s", applicationProperties.APP_DATA_SERVICE_CENTER_URL), e);
        }
    }
}
