package com.project.adpushup.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.adpushup.config.AppConfig;
import com.project.adpushup.pojo.AdvertiserRevenue;
import com.project.adpushup.pojo.SiteAdvertiser;
import com.project.adpushup.pojo.SiteRevenue;

import static com.project.adpushup.constant.Constants.BATCH_SIZE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class DataFileDao {

    Logger logger = LoggerFactory.getLogger(DataFileDao.class);

    AppConfig appConfig = new AppConfig();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(appConfig.getDataSource());

    public void addSiteAdvertiserData(List<SiteAdvertiser> siteAdvertiserList) throws ClassNotFoundException {

        List<String> failedSiteAdvIdList = new ArrayList<>();
        int[][] updateCounts = jdbcTemplate.batchUpdate(
                "insert into site_advertiser (site_id,advertiser_id) values(?,?)",
                siteAdvertiserList,
                BATCH_SIZE, // batchsize
                new ParameterizedPreparedStatementSetter<SiteAdvertiser>() {
                    public void setValues(PreparedStatement ps, SiteAdvertiser argument) {
                        try {
                            ps.setString(1, argument.getSiteId());
                            ps.setString(2, argument.getAdvertiserId());
                        } catch (SQLException e) {
                            failedSiteAdvIdList.add(argument.getSiteId() + "-" + argument.getAdvertiserId());
                            e.printStackTrace();
                        }
                    }
                });
        int dataWrittenSize = siteAdvertiserList.size() - failedSiteAdvIdList.size();
        System.out.println(Thread.currentThread().getName() + "->written data size in table site_advertiser-->" + dataWrittenSize);
        for (String siteAdvId : failedSiteAdvIdList) {
            System.out.println("failed sitId-AdvertiserId for table:site_advertiser->" + siteAdvId);
        }
    }

    public void addAdvertiserRevenueData(List<AdvertiserRevenue> advertiserRevenueList) {
        // TODO Auto-generated method stub
        List<String> failedAdvIdList = new ArrayList<>();
        int[][] updateCounts = jdbcTemplate.batchUpdate(
                "insert into advertiser_revenue (advertiser_id,revenue) values(?,?)",
                advertiserRevenueList,
                BATCH_SIZE, // batchsize
                new ParameterizedPreparedStatementSetter<AdvertiserRevenue>() {
                    public void setValues(PreparedStatement ps, AdvertiserRevenue argument) {
                        try {
                            ps.setString(1, argument.getAdvertiserId());
                            ps.setBigDecimal(2, argument.getRevenue());
                        } catch (SQLException e) {
                            failedAdvIdList.add(argument.getAdvertiserId());
                            e.printStackTrace();
                        }
                    }
                });
        int dataWrittenSize = advertiserRevenueList.size() - failedAdvIdList.size();
        System.out.println(Thread.currentThread().getName() + "->written data size in table advertiser_revenue-->" + dataWrittenSize);
        for (String advId : failedAdvIdList) {
            System.out.println("failed AdvertiserId for table:advertiser_revenue->" + advId);
        }

    }

    public void addSiteRevenueData(List<SiteRevenue> siteRevenueList) {
        // TODO Auto-generated method stub

        int[][] updateCounts = jdbcTemplate.batchUpdate(
                "insert into site_revenue (site_id,revenue) values(?,?)",
                siteRevenueList, 1,
                new ParameterizedPreparedStatementSetter<SiteRevenue>() {
                    public void setValues(PreparedStatement ps, SiteRevenue argument) {
                        try {
                            ps.setString(1, argument.getSiteId());
                            ps.setBigDecimal(2, argument.getSRevenue());
                        } catch (SQLException e) {
                            System.out.println("failed siteId for table:site_revenue->" + argument.getSiteId());
                        }
                    }
                });
        System.out.println(Thread.currentThread().getName() + "->siteAdvertiserList size-->" + 1);
    }
}
