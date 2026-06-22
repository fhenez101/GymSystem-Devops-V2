package com.gym.gym_system.service;

import com.gym.gym_system.entity.Membership;
import com.gym.gym_system.repository.MembershipRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MembershipNotificationService {

    private final MembershipRepository membershipRepository;
    private final WhatsappService whatsappService;

    public MembershipNotificationService(MembershipRepository membershipRepository,
                                         WhatsappService whatsappService) {
        this.membershipRepository = membershipRepository;
        this.whatsappService = whatsappService;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void sendDueTodayNotifications() {
        List<Membership> dueToday = membershipRepository
                .findByEndDateAndStatusAndWhatsappSentFalse(LocalDate.now(), "ACTIVE");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM", new java.util.Locale("es", "CR"));

        for (Membership membership : dueToday) {
            if (membership.getClient() != null && membership.getClient().getPhone() != null) {
                String dueDate = membership.getEndDate().format(formatter);

                whatsappService.sendMembershipDueMessage(
                        membership.getClient().getPhone(),
                        dueDate
                );

                membership.setWhatsappSent(true);
                membershipRepository.save(membership);
            }
        }
    }
}