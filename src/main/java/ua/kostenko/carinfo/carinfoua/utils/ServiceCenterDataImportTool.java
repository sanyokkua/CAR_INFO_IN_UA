package ua.kostenko.carinfo.carinfoua.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.carinfoua.configuration.ApplicationProperties;
import ua.kostenko.carinfo.carinfoua.data.ServiceCenterData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceCenterDataImportTool {
  private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCenterDataImportTool.class);
  private ApplicationProperties applicationProperties;

  @Autowired
  public ServiceCenterDataImportTool(ApplicationProperties applicationProperties) {
    Preconditions.checkNotNull(applicationProperties);
    this.applicationProperties = applicationProperties;
  }

  public void initiServiceCenterData() {
    Document doc = null;
    try {
      doc = Jsoup.connect(applicationProperties.APP_DATA_SERVICE_CENTER_URL).get();
    } catch (IOException e) {
      LOGGER.error("Error with parsing url", e);
    }
    List<ServiceCenterData> resultList = new ArrayList<>();
    Elements newsHeadlines = doc.select("#post-54 > div > div.mapua_form.mapua_region_body > table > tbody > tr.region_item");
    for (Element headline : newsHeadlines) {
      List<Element> childNode = headline.childNodes().stream().filter(node -> node instanceof Element).map(node -> ((Element)node)).filter(element -> StringUtils.isNotBlank(element.text())).collect(Collectors.toList());
      String dep = childNode.get(0).text().trim();
      String addr = childNode.get(1).text().trim();
      String emai = childNode.get(4).text().trim();
      ServiceCenterData serviceCenterData = new ServiceCenterData(Long.valueOf(dep), addr, emai);
      resultList.add(serviceCenterData);
    }
    resultList.forEach(serviceCenterData -> LOGGER.info("Service center: id {}, address {}, email {}", serviceCenterData.getDepId(), serviceCenterData.getAddress(), serviceCenterData.getEmail()));
    LOGGER.info("Finish parsing");
  }

}
