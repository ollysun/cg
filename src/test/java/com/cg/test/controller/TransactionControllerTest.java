package com.cg.test.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.cg.test.model.Transaction;
import com.cg.test.model.TransactionStatisticResponse;
import com.cg.test.service.TransactionsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;

import org.hamcrest.Matchers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {TransactionController.class})
@ExtendWith(SpringExtension.class)
public class TransactionControllerTest {
    @Autowired
    private TransactionController transactionController;

    @MockBean
    private TransactionsService transactionsService;

    @Test
    public void testAddTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(new Timestamp(1L));
        transaction.setAmount("Amount");
        String content = (new ObjectMapper()).writeValueAsString(transaction);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.transactionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        doNothing().when(this.transactionsService).deleteTransaction();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/transactions");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.transactionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    public void testGetTransactionStatistics() throws Exception {
        when(this.transactionsService.getTransactionStatistic())
                .thenReturn(new TransactionStatisticResponse("Sum", "Avg", "Min", "Max", 3L));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statistics");
        MockMvcBuilders.standaloneSetup(this.transactionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers
                                .containsString("{\"sum\":\"Sum\",\"avg\":\"Avg\",\"min\":\"Min\",\"max\":\"Max\",\"count\":3}")));
    }

    @Test
    public void testGetTransactionStatisticsUpProcessableBuild() throws Exception {
        when(this.transactionsService.getTransactionStatistic()).thenReturn(null);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statistics");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.transactionController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(422));
    }
}

