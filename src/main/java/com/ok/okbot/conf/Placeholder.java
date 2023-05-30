package com.ok.okbot.conf;

public enum Placeholder {
    SHARE_CONTACT("share_contact"), MAIN_MENU("main_menu"), AGREEMENT_AWAIT("agreement_await"),
    CONTACTS("contacts"), QA("qa"), NOTIFICATION_DESCRIPTION("notification_description"),
    PARTNERS_DESCRIPTION("partners_description"), GRATITUDE("gratitude"),
    WHAT_WE_CAN_DO("what_can_we_do"), ENTER_DATE_MESSAGE("enter_date_message"),
    DATE_ADDED_MESSAGE("date_added_message"), WRONG_FORMAT_MESSAGE("wrong_format_message"),
    NEXT_DONATION_MESSAGE("next_donation_message");

    Placeholder(String placeholder) {
        this.value = placeholder;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
