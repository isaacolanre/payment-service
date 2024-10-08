package com.payment.service.models;

import com.payment.service.enumerations.MessageChannel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "notification_device")
@EqualsAndHashCode(callSuper = false)
public class NotificationDevice extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageChannel messageChannel;

    private String token;

    private UUID publicId = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    public NotificationDevice(MessageChannel messageChannel, String token, Account account) {
        this.messageChannel = messageChannel;
        this.token = token;
        this.account = account;
    }
}
