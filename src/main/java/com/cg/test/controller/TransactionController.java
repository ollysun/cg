package com.cg.test.controller;

import com.cg.test.constant.DATESTATUS;
import com.cg.test.exception.TransactionGlobalException;
import com.cg.test.model.Transaction;
import com.cg.test.model.TransactionStatisticResponse;
import com.cg.test.service.TransactionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RestController
public class TransactionController {

    @Autowired
    private TransactionsService transactionService;

    @PostMapping("/transaction")
    public ResponseEntity<Object> addTransaction(@RequestBody @Valid Transaction transaction) {
        DATESTATUS datestatus = transactionService.addTransaction(transaction);
        if(datestatus == DATESTATUS.OLDER_THAN_MINUTE){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else if(datestatus == DATESTATUS.FUTURE){
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }else{
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @ResponseStatus(HttpStatus.OK )
    @GetMapping("/statistics")
    public TransactionStatisticResponse getTransactionStatistics() {
        TransactionStatisticResponse transactionStatisticResponse = transactionService.getTransactionStatistic();
        if(transactionStatisticResponse  == null){
            throw new TransactionGlobalException("Empty List");
        }
        return transactionService.getTransactionStatistic();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT )
    @DeleteMapping(value = "/transactions")
    public void deleteTransaction(){
        transactionService.deleteTransaction();
    }


}
