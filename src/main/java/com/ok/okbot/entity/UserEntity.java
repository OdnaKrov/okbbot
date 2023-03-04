package com.ok.okbot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Entity(name = "telegram_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_agreement")
    private Boolean userAgreement;

    @Column(name = "command")
    private String command;

    @Column(name = "step")
    private Long step;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(userAgreement, that.userAgreement) && Objects.equals(command, that.command) && Objects.equals(step, that.step) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstName, lastName, userAgreement, command, step, phoneNumber);
    }
}
