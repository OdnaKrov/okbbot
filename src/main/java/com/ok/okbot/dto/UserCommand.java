package com.ok.okbot.dto;

public enum UserCommand {
    NOTIFICATIONS("/notifications"), PARTNER_BONUS("/partner_bonus"),
    QA("/qa"), CONTACTS("/contacts");

    UserCommand(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }

    public static UserCommand byValue(String value) {
        for (UserCommand command : UserCommand.values()) {
            if (command.getValue().equals(value)) {
                return command;
            }
        }

        return null;
    }
}
