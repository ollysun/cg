package com.cg.test.service;


import com.cg.test.constant.DATESTATUS;
import com.cg.test.model.TimeDifference;
import com.cg.test.model.Transaction;
import com.cg.test.model.TransactionStatisticResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Component
@Slf4j
public class TransactionsService {

    private static final List<Transaction> transactionList =  new ArrayList<>();


    public TransactionStatisticResponse getTransactionStatistic(){

        BigDecimal avg ;
        BigDecimal max;
        BigDecimal min;
        BigDecimal sum;

        if (transactionList.isEmpty()) {
                return null;
        }
        List<Double> amount = new ArrayList<>();
        for (Transaction transaction:transactionList) {
            amount.add(Double.parseDouble(transaction.getAmount()));
        }
        log.info("amount " + amount);

        double avgVal = amount.stream()
                           .mapToDouble(Double::doubleValue)
                           .summaryStatistics().getAverage();
        avg = BigDecimal.valueOf(avgVal).setScale(2, RoundingMode.HALF_UP);

        double maxValue = amount.stream()
                           .mapToDouble(Double::doubleValue)
                           .summaryStatistics().getMax();
        max = BigDecimal.valueOf(maxValue).setScale(2, RoundingMode.HALF_UP);

        double minValue = amount.stream()
                .mapToDouble(Double::doubleValue)
                .summaryStatistics().getMin();
        min = BigDecimal.valueOf(minValue).setScale(2, RoundingMode.HALF_UP);

        double sumValue = amount.stream()
                                .mapToDouble(Double::doubleValue)
                                .sum();
        sum = BigDecimal.valueOf(sumValue).setScale(2, RoundingMode.HALF_UP);

        long countValue = amount.stream()
                .mapToDouble(Double::doubleValue)
                .summaryStatistics().getCount();

        return new TransactionStatisticResponse(sum.toString(),avg.toString(),min.toString(),
                max.toString(),countValue);
    }

    public DATESTATUS addTransaction(Transaction transaction){
        DATESTATUS datestatus = null;
        Date dateFrom = formattingTimestamp(transaction);
        TimeDifference timeDifference = printDifference(dateFrom, new Date());

        if (timeDifference.getDay() > 1 || timeDifference.getDifference() < 1){
            datestatus = DATESTATUS.FUTURE;
        }else if(timeDifference.getDay() == 0 &&
                timeDifference.getHours() == 0 &&
                timeDifference.getMinutes() > 1 &&
                timeDifference.getSeconds() > 60){
            datestatus = DATESTATUS.OLDER_THAN_MINUTE;
        }else if(timeDifference.getDay() == 0 &&
                timeDifference.getHours() == 0 &&
                timeDifference.getMinutes() <= 1 &&
                timeDifference.getSeconds() <= 60) {
            transactionList.add(transaction);
            datestatus = DATESTATUS.SAMEDAY;
        }
        return datestatus;
    }

    public void deleteTransaction(){
        transactionList.clear();
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    private static TimeDifference printDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        log.info("startDate : " + startDate);
        log.info("endDate : "+ endDate);
        log.info("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return new TimeDifference(different,
                elapsedDays,elapsedHours,elapsedMinutes,elapsedSeconds);
    }


    private Date formattingTimestamp(Transaction transaction) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(transaction.getTimestamp());
        cal.add(Calendar.HOUR, -1);
        return cal.getTime();
    }

}
