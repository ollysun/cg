package com.cg.test.integration;

import com.cg.test.CgTestApplication;
import com.cg.test.constant.DATESTATUS;
import com.cg.test.controller.TransactionController;
import com.cg.test.model.Transaction;
import com.cg.test.model.TransactionStatisticResponse;
import com.cg.test.service.TransactionsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ContextConfiguration(classes = { CgTestApplication.class })
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TransactionController.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionIntegrationTest {

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @MockBean
    private TransactionsService transactionService;

    private final TransactionStatisticResponse transactionStatisticResponse = new TransactionStatisticResponse();

    private List<Transaction> transactionList =  new ArrayList<>();

    public TransactionIntegrationTest() {
    }

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void testAddTransactionWithUnprocessedEntity() throws Exception {
        Date date1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                .parse("2021-09-27T00:50:13.345Z");
        Transaction transaction = new Transaction("12.3243",new Timestamp(date1.getTime()));
        when(transactionService.addTransaction(any(Transaction.class))).thenReturn(DATESTATUS.FUTURE);
        ResponseEntity<Object> responseEntity = this.restTemplate
                .postForEntity(getRootUrl() + "/transaction", transaction, Object.class);
        assertEquals(422, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testAddTransactionWithBadRequest() throws Exception {
        Date date1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                .parse("2020-09-27T00:50:13.345Z");
        Transaction transaction = new Transaction("",new Timestamp(date1.getTime()));
        ResponseEntity<Object> responseEntity = this.restTemplate
                .postForEntity(getRootUrl() + "/transaction", transaction, Object.class);
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testVerifyStatistic() throws Exception {
        Date date1=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                .parse("2020-09-27T00:50:13.345Z");
        Date date2=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                .parse("2020-09-27T00:50:13.345Z");
        Date date3=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                .parse("2020-09-27T00:50:13.345Z");
        Transaction transaction = new Transaction("5.0",new Timestamp(date1.getTime()));
        Transaction transaction2 = new Transaction("5.1",new Timestamp(date1.getTime()));
        Transaction transaction3 = new Transaction("5.2",new Timestamp(date1.getTime()));
        transactionList.add(transaction);
        transactionList.add(transaction2);
        transactionList.add(transaction3);
        List<Double> amount = new ArrayList<>();
        for (Transaction transactionfor:transactionList) {
            amount.add(Double.parseDouble(transactionfor.getAmount()));
        }
        double sumValue = amount.stream()
                                .mapToDouble(Double::doubleValue)
                                .summaryStatistics()
                                .getSum();
        transactionStatisticResponse.setSum(String.valueOf(sumValue));
        when(transactionService.getTransactionStatistic()).thenReturn(transactionStatisticResponse);
        ResponseEntity<TransactionStatisticResponse> responseEntity = this.restTemplate
                .getForEntity(getRootUrl() + "/statistics", TransactionStatisticResponse.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void deleteTransaction() {
        doNothing().when(transactionService).deleteTransaction();
        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<Void> responseEntity = this.restTemplate
                .exchange(getRootUrl() + "/transactions", HttpMethod.DELETE, entity, (Class<Void>) null);
        assertEquals(204, responseEntity.getStatusCodeValue());
    }
}
