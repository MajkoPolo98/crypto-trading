package com.cryptoGame.controller;

import com.cryptoGame.domain.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("/v1")
public class TransactionController {



    @PutMapping("/transactions/buy")
    private void buyCrypto(){

    }

    private void sellCrypto(){

    }

    @GetMapping("/transactions")
    private ResponseEntity<List<Transaction>> getAllTransactions(){
        return new ResponseEntity(new ArrayList<Transaction>(), HttpStatus.OK);
    }

    @GetMapping("/transactions/{id}")
    private ResponseEntity getTransaction(@PathVariable("id") Long id){
        return new ResponseEntity(new Transaction(), HttpStatus.OK);
    }

}
