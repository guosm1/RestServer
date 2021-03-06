package com.asiainfo.ocmanager.rest.resource.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

/**
 * 
 * @author zhaoyim
 *
 */
public class QuotaCommonUtils {

	private static Logger logger = LoggerFactory.getLogger(QuotaCommonUtils.class);

	public static Map<String, String> parserQuota(String service, JsonObject serviceQuota) {
		Map<String, String> quotaMap = new HashMap<String, String>();

		switch (service.toLowerCase()) {
		case "hdfs":

			String nameSpaceQuota = serviceQuota.get("nameSpaceQuota") == null ? null
					: serviceQuota.get("nameSpaceQuota").getAsString();
			String storageSpaceQuota = serviceQuota.get("storageSpaceQuota") == null ? null
					: serviceQuota.get("storageSpaceQuota").getAsString();

			quotaMap.put("nameSpaceQuota", nameSpaceQuota);
			quotaMap.put("storageSpaceQuota", storageSpaceQuota);
			break;
		case "hbase":

			String maximumTablesQuota = serviceQuota.get("maximumTablesQuota") == null ? null
					: serviceQuota.get("maximumTablesQuota").getAsString();
			String maximumRegionsQuota = serviceQuota.get("maximumRegionsQuota") == null ? null
					: serviceQuota.get("maximumRegionsQuota").getAsString();

			quotaMap.put("maximumTablesQuota", maximumTablesQuota);
			quotaMap.put("maximumRegionsQuota", maximumRegionsQuota);
			break;
		case "hive":

			String storageSpaceQuotaHive = serviceQuota.get("storageSpaceQuota") == null ? null
					: serviceQuota.get("storageSpaceQuota").getAsString();
			String yarnQueueQuotaHive = serviceQuota.get("yarnQueueQuota") == null ? null
					: serviceQuota.get("yarnQueueQuota").getAsString();

			quotaMap.put("storageSpaceQuota", storageSpaceQuotaHive);
			quotaMap.put("yarnQueueQuota", yarnQueueQuotaHive);
			break;
		case "mapreduce":

			String yarnQueueQuotaMapreduce = serviceQuota.get("yarnQueueQuota") == null ? null
					: serviceQuota.get("yarnQueueQuota").getAsString();

			quotaMap.put("yarnQueueQuota", yarnQueueQuotaMapreduce);
			break;
		case "spark":

			String yarnQueueQuotaSpark = serviceQuota.get("yarnQueueQuota") == null ? null
					: serviceQuota.get("yarnQueueQuota").getAsString();

			quotaMap.put("yarnQueueQuota", yarnQueueQuotaSpark);
			break;
		case "kafka":

			String topicTTL = serviceQuota.get("topicTTL") == null ? null : serviceQuota.get("topicTTL").getAsString();
			String topicQuota = serviceQuota.get("topicQuota") == null ? null
					: serviceQuota.get("topicQuota").getAsString();
			String partitionSize = serviceQuota.get("partitionSize") == null ? null
					: serviceQuota.get("partitionSize").getAsString();

			quotaMap.put("topicTTL", topicTTL);
			quotaMap.put("topicQuota", topicQuota);
			quotaMap.put("partitionSize", partitionSize);
			break;
		default:
			logger.error("The {} service did NOT support the set quota in tenant, please check with admin.", service);
		}

		return quotaMap;
	}

	public static String logAndResStr(long quota, String param, String service) {
		logger.info("NOT enough " + service + " " + param + ", it need more: {}\n", -quota);
		return "NOT enough " + service + " " + param + " to crteate the tenant, it need more quota: " + (-quota) + "; ";
	}
}
