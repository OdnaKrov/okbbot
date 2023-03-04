package com.ok.okbot.conf;

public enum Placeholder {
    SHARE_CONTACT("share_contact"), MAIN_MENU("main_menu"), AGREEMENT_AWAIT("agreement_await"),
    CONTACTS("contacts"), QA("qa"), NOTIFICATION_DESCRIPTION("notification_description");

    Placeholder(String placeholder) {
        this.value = placeholder;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
