package com.manza.transactions.service;

import com.manza.transactions.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransactionServiceTest {

    private static final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void shouldDoNothing(){
        System.out.println("blas");
    }
}
