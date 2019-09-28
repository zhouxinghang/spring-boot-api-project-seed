package com.company.project.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhouxinghang
 * @date 2019-09-28
 */
@Slf4j
public class TimeUtils {
    static DateTimeFormatter simpleDateFormat_yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    static DateTimeFormatter simpleDateFormat_yyyyMMdd       = DateTimeFormatter.ofPattern("yyyyMMdd");
    static DateTimeFormatter simpleDateFormat_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static DateTimeFormatter simpleDateFormat_MMdd           = DateTimeFormatter.ofPattern("MMdd");
    static DateTimeFormatter YYYY_MM_DD_HH_MM_SS_FORMAT      = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static DateTimeFormatter YYYY_MM_DD_HH_MM_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    static DateTimeFormatter YYYY_MM_DD_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static String now() {
        LocalDateTime now = LocalDateTime.now();
        return simpleDateFormat_yyyyMMddHHmmss.format(now);
    }
    public static String formatDate(LocalDate localDate) {
        return simpleDateFormat_yyyyMMdd.format(localDate);
    }

    public static String transferLocalDate(LocalDate localDate){
        return simpleDateFormat_YYYY_MM_DD.format(localDate);
    }

    public static String formatDate(String date){
        LocalDateTime dateTime = LocalDateTime.parse(date, YYYY_MM_DD_HH_MM_SS_FORMAT);
        return dateTime.format(simpleDateFormat_yyyyMMdd);
    }



    public static String formatDate(String pattern, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static LocalDateTime parseDateTime( String dateTime){
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, YYYY_MM_DD_HH_MM_FORMAT);
        return localDateTime;
    }

    /**
     * tbms消费mafka，是这种时间格式
     * @return
     */
    public static String nowFormateSS() {
        LocalDateTime now = LocalDateTime.now();
        return YYYY_MM_DD_HH_MM_SS_FORMAT.format(now);
    }

    /**
     * 按照传入的日期时间格式进行处理
     * @param pattern 日期时间格式
     * @param dateTime 时间
     * @return
     */
    public static String formatDateTime(String pattern, LocalDateTime dateTime){
        //pattern MMM d yyyy  hh:mma Dec 12 2017  5:06PM
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        return dateTimeFormatter.format(dateTime);
    }

    public static String today() {
        return simpleDateFormat_MMdd.format(LocalDate.now());
    }

    public static void sleepRandomTime(){
        try {
            Thread.sleep(RandomUtils.nextInt(1500, 2500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void sleepRandomTime(int start,int end){
        try {
            Thread.sleep(RandomUtils.nextInt(start,end));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static int getSleepTimeFromConf(String timeConf){
        if (StringUtils.isNotBlank(timeConf)) {
            String[] timeArray = timeConf.split(",");
            try {
                return RandomUtils.nextInt(Integer.valueOf(timeArray[0]), Integer.valueOf(timeArray[1]));
            } catch (Exception e) {
                log.error("parse sleepTime Error,{}", timeConf, e);
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static String formateDateStrMM(String date) {
        StringBuilder builder = new StringBuilder();

        try {
            builder.append(date.substring(0, 4)).append("-").append(date.substring(4, 6)).append("-").append(date.substring(6, 8));
        } catch (Exception var3) {
            log.error("formateDateStrMM error date {}", date, var3);
        }

        return builder.toString();
    }


    public static void sleepTime(int sleepTime){
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static String getEndDate(String startDate){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try{
            LocalDate localStartDate = LocalDate.parse(startDate, dateFormat);
            LocalDate localEndDate = localStartDate.plusDays(30);
            String endDate = localEndDate.format(dateFormat);
            return endDate;
        }catch (Exception e){
            log.error("日期解析错误", e);
        }

        return StringUtils.EMPTY;

    }
}
