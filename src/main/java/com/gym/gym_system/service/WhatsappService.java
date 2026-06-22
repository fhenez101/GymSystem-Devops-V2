package com.gym.gym_system.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsappService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromWhatsapp;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendMembershipDueMessage(String toPhone, String dueDateFormatted) {
        String body = """
                Buenos días!
                Estimado cliente:
                Moar Fitness Center le recuerda que su fecha de pago es el día %s.
                Puede cancelar vía sinpe móvil o efectivo en recepción.
                Agradecemos su puntualidad.
                """.formatted(dueDateFormatted);

        Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:" + toPhone),
                new com.twilio.type.PhoneNumber("whatsapp:" + fromWhatsapp),
                body
        ).create();
    }
}