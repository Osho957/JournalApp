package com.osho.journalApp.scheduler;

import com.osho.journalApp.entities.JournalEntry;
import com.osho.journalApp.entities.User;
import com.osho.journalApp.enums.Sentiment;
import com.osho.journalApp.model.SentimentData;
import com.osho.journalApp.repository.UserRepositoryImpl;
import com.osho.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    private final EmailService emailService;
    private final UserRepositoryImpl userRepository;
    private final KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Autowired
    public UserScheduler(EmailService emailService, UserRepositoryImpl userRepository,KafkaTemplate<String, SentimentData> kafkaTemplate) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(cron = "0 0 12 * * SUN")
    public void fetchUsersAndSendMail() {
        List<User> users = userRepository.getUserForSentimentalAnalysis();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(7))).map(JournalEntry::getSentiment).collect(Collectors.toList());
            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if (sentiment != null)
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
           if (mostFrequentSentiment != null) {
               SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + mostFrequentSentiment).build();
               try{
                   kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
               }catch (Exception e){
                   emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
               }
            }
        }
    }
}
