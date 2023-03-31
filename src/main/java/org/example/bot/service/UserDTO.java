package org.example.bot.service;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@ToString
@Getter
public class UserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String languageCode;
    private LocalDate date;

    public static class Builder {
        private final UserDTO newUserDTO;

        public Builder(){
            this.newUserDTO = new UserDTO();
        }

        public Builder withId(long id) {
            this.newUserDTO.id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.newUserDTO.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.newUserDTO.lastName = lastName;
            return this;
        }

        public Builder withUserName(String userName) {
            this.newUserDTO.userName = userName;
            return this;
        }

        public Builder withLanguageCode(String languageCode) {
            this.newUserDTO.languageCode = languageCode;
            return this;
        }

        public Builder withDate(long date) {
            this.newUserDTO.date = parseDate(date);
            return this;
        }

        public UserDTO builder() {
            return newUserDTO;
        }

        private LocalDate parseDate(long dateInSeconds) {
            return Instant.ofEpochSecond(dateInSeconds).atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }
}
