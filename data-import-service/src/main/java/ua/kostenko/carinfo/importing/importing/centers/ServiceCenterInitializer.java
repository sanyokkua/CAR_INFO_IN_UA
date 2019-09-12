package ua.kostenko.carinfo.importing.importing.centers;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.importing.configuration.ApplicationProperties;
import ua.kostenko.carinfo.importing.importing.Initializer;
import ua.kostenko.carinfo.importing.importing.Persist;

@Component
@Slf4j
public class ServiceCenterInitializer implements Initializer {

    private static final Pattern DIGITS_ONLY = Pattern.compile("\\d+");
    private final ApplicationProperties applicationProperties;
    private final Persist<ServiceCenter> persist;

    @Autowired
    public ServiceCenterInitializer(@NonNull @Nonnull ApplicationProperties applicationProperties,
            @NonNull @Nonnull Persist<ServiceCenter> persist) {
        this.applicationProperties = applicationProperties;
        this.persist = persist;
    }

    @Override
    public void init() {
        log.info("init: Starting initialization of ServiceCenters data.");
        try {
            log.info("Opening page by url {}", applicationProperties.appDataServiceCenterUrl);
            Document htmlPageDocument = Jsoup.connect(applicationProperties.appDataServiceCenterUrl).get();
            if (Objects.nonNull(htmlPageDocument)) {
                log.info("init: Parsing  all information from page {}",
                        applicationProperties.appDataServiceCenterUrl);
                List<ServiceCenter> resultList = new ArrayList<>();
                Elements tbody = htmlPageDocument.select(applicationProperties.appDataServiceCenterCssSelector);
                for (Element tr : tbody) {
                    List<Element> childTd = tr.childNodes()
                            .stream()
                            .filter(node -> node instanceof Element)
                            .map(node -> ((Element) node))
                            .filter(element -> isNotBlank(element.text()))
                            .collect(Collectors.toList());
                    if (childTd.size() > 4) {
                        String departmentId = childTd.get(0).text().trim();
                        Matcher matcher = DIGITS_ONLY.matcher(departmentId);
                        long depId = 0;
                        if (matcher.find()) {
                            depId = Long.valueOf(matcher.group());
                        }
                        String address = childTd.get(1).text().trim();
                        String email = childTd.get(4).text().trim();
                        ServiceCenter serviceCenterEntity = new ServiceCenter(depId, address, email);
                        resultList.add(serviceCenterEntity);
                    }
                }
                resultList.forEach(
                        serviceCenterEntity -> log.info("init: Service center: {}", serviceCenterEntity.toString()));
                resultList.stream().parallel().forEach(persist::persist);
                log.info("init: All information parsed");
            }
            log.info("init: Finished initialization of ServiceCenters data.");
        } catch (IOException e) {
            log.error(String.format("init: Error with parsing or connecting to url: %s",
                    applicationProperties.appDataServiceCenterUrl), e);
        }
    }
}
