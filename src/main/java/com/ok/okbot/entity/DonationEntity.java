package com.ok.okbot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Entity(name = "donation")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DonationEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "date")
    private LocalDate date;
}
