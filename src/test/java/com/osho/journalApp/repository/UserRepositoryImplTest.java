package com.osho.journalApp.repository;

import com.osho.journalApp.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    public void getUserForSentimentalAnalysisTest() {
         List<User> user = userRepositoryImpl.getUserForSentimentalAnalysis();
         Assertions.assertNotNull(user);
         assert user.get(0).getUsername().equals("test_user");
    }
}
