package com.ok.okbot.dto;

public enum UserState {
    AGREEMENT_AWAIT("agreement_await"), MAIN_MENU("main_menu"),
    WHAT_CAN_WE_DO("what_can_we_do"), NOTIFICATION("notification");

    UserState(String label) {
        this.label = label;
    }

    private final String label;

    public String getLabel() {
        return label;
    }

    public static UserState byLabel(String label) {
        for (UserState state : values()) {
            if (state.getLabel().equals(label)) {
                return state;
            }
        }

        return null;
    }
}
