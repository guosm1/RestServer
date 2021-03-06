package com.asiainfo.ocmanager.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.ocmanager.rest.bean.ServiceInstanceDefaultQuotaBean;
import com.asiainfo.ocmanager.rest.resource.ServiceResource;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author zhaoyim
 *
 */
public class ServicesDefaultQuotaConf {

	private static Logger logger = LoggerFactory.getLogger(ServicesDefaultQuotaConf.class);

	private static Map<String, Map<String, ServiceInstanceDefaultQuotaBean>> servicesDefaultQuota = new HashMap<String, Map<String, ServiceInstanceDefaultQuotaBean>>();

	public static Map<String, Map<String, ServiceInstanceDefaultQuotaBean>> getInstance() {
		if (servicesDefaultQuota == null || servicesDefaultQuota.isEmpty()) {
			synchronized (ServicesDefaultQuotaConf.class) {
				if (servicesDefaultQuota == null || servicesDefaultQuota.isEmpty()) {
					new ServicesDefaultQuotaConf();
				}
			}
		}
		return servicesDefaultQuota;
	}

	private ServicesDefaultQuotaConf() {
		loadData();
	}

	private void loadData() {

		String servicesStr;
		try {
			servicesStr = ServiceResource.callDFToGetAllServices();

			JsonObject servicesJson = new JsonParser().parse(servicesStr).getAsJsonObject();
			JsonArray items = servicesJson.getAsJsonArray("items");
			if (items != null) {
				for (int i = 0; i < items.size(); i++) {
					JsonObject spec = items.get(i).getAsJsonObject().getAsJsonObject("spec");
					String name = spec.get("name").getAsString();
					JsonArray plan = spec.getAsJsonArray("plans");

					JsonObject metadata = plan.get(0).getAsJsonObject().getAsJsonObject("metadata");
					if (!metadata.get("customize").isJsonNull()) {
						servicesDefaultQuota.put(name.toLowerCase(),
								parserServiceQuota(name.toLowerCase(), metadata.getAsJsonObject("customize")));
					}

				}
			}
		} catch (Exception e) {
			logger.error("Error while initial the services default quota.", e);
		}

	}

	private static Map<String, ServiceInstanceDefaultQuotaBean> parserServiceQuota(String service, JsonObject quota) {

		Map<String, ServiceInstanceDefaultQuotaBean> quotaMap = new HashMap<String, ServiceInstanceDefaultQuotaBean>();

		switch (service.toLowerCase()) {
		case "hdfs":

			quotaMap.put("nameSpaceQuota", new ServiceInstanceDefaultQuotaBean(quota.getAsJsonObject("nameSpaceQuota")));
			quotaMap.put("storageSpaceQuota", new ServiceInstanceDefaultQuotaBean(quota.getAsJsonObject("storageSpaceQuota")));

			break;
		case "hbase":

			quotaMap.put("maximumTablesQuota",
					new ServiceInstanceDefaultQuotaBean(quota.getAsJsonObject("maximumTablesQuota")));
			quotaMap.put("maximumRegionsQuota",
					new ServiceInstanceDefaultQuotaBean(quota.getAsJsonObject("maximumRegionsQuota")));

			break;
		case "hive":

			quotaMap.put("storageSpaceQuota", new ServiceInstanceDefaultQuotaBean(quota.getAsJsonObject("storageSpaceQuota")));
			quotaMap.put("yarnQueueQuota", new ServiceInstanceDefaultQuotaBean(quota.getAsJsonObject("yarnQueueQuota")));

			break;
		case "mapreduce":
			quotaMap.put("yarnQueueQuota", new ServiceInstanceDefaultQuotaBean(quota.getAsJsonObject("yarnQueueQuota")));

			break;
		case "spark":

			quotaMap.put("yarnQueueQuota", new ServiceInstanceDefaultQuotaBean(quota.getAsJsonObject("yarnQueueQuota")));

			break;
		case "kafka":

			quotaMap.put("topicTTL", new ServiceInstanceDefaultQuotaBean(quota.getAsJsonObject("topicTTL")));
			quotaMap.put("topicQuota", new ServiceInstanceDefaultQuotaBean(quota.getAsJsonObject("topicQuota")));
			quotaMap.put("partitionSize", new ServiceInstanceDefaultQuotaBean(quota.getAsJsonObject("partitionSize")));

			break;
		default:
			logger.error("The {} service did NOT support the set quota in tenant, please check with admin.", service);
		}

		return quotaMap;
	}

}
