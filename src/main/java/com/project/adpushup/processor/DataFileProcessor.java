package com.project.adpushup.processor;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.project.adpushup.dao.DataFileDao;
import com.project.adpushup.pojo.AdvertiserRevenue;
import com.project.adpushup.pojo.SiteAdvertiser;
import com.project.adpushup.pojo.SiteRevenue;

public class DataFileProcessor implements Runnable {

    private final List<SiteAdvertiser> siteAdvertiserList = new ArrayList<>();
    private final List<AdvertiserRevenue> advertiserRevenueList = new ArrayList<>();
    private final List<SiteRevenue> siteRevenueList = new ArrayList<>();
    private BigDecimal siteRevenueSum = new BigDecimal(0);
    private String siteId;
    private Scanner scanner;
    DataFileDao dataFileDao = new DataFileDao();

    public DataFileProcessor(String fileName, Scanner scanner) {
        this.siteId = fileName;
        this.scanner = scanner;
    }

    @Override
    public void run() {

        while (scanner.hasNext()) {
            String[] dataArrayStr = scanner.next().split(",");
            // Skip Headers
            if ("AdvertiserID".equalsIgnoreCase(dataArrayStr[0]) || "Revenue".equalsIgnoreCase(dataArrayStr[1])) {
                continue;
            }
            BigDecimal revenue = BigDecimal.ZERO;
            try {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator(',');
                symbols.setDecimalSeparator('.');
                String pattern = "#,##0.0#";
                DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
                decimalFormat.setParseBigDecimal(true);

                // parse the revenue string
                if (dataArrayStr[1] != null && !"".equals(dataArrayStr[1].trim())) {
                    revenue = (BigDecimal) decimalFormat.parse(dataArrayStr[1]);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            siteRevenueSum = siteRevenueSum.add(revenue);
            SiteAdvertiser siteAdvertiser = SiteAdvertiser.builder().siteId(siteId).advertiserId(dataArrayStr[0]).build();
            siteAdvertiserList.add(siteAdvertiser);
            AdvertiserRevenue advertiserRevenue = AdvertiserRevenue.builder().advertiserId(dataArrayStr[0]).revenue(revenue).build();
            advertiserRevenueList.add(advertiserRevenue);
        }

        SiteRevenue siteRevenue = SiteRevenue.builder().siteId(siteId).sRevenue(siteRevenueSum).build();
        siteRevenueList.add(siteRevenue);

        try {
            dataFileDao.addSiteAdvertiserData(siteAdvertiserList);
            dataFileDao.addAdvertiserRevenueData(advertiserRevenueList);
            dataFileDao.addSiteRevenueData(siteRevenueList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
