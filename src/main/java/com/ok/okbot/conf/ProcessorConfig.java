package com.ok.okbot.conf;

import com.ok.okbot.dto.UserState;
import com.ok.okbot.service.StateProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProcessorConfig {
    private final List<StateProcessor> stateProcessors;

    public StateProcessor getStateProcessor(UserState state, String option) {
        return stateProcessors.stream()
                .filter(p -> p.getState().equals(state) && p.getOption().equals(option))
                .findFirst()
                .orElseThrow();
    }
}
