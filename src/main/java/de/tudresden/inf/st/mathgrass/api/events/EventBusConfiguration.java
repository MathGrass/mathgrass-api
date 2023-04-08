package de.tudresden.inf.st.mathgrass.api.events;

import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventBusConfiguration {
    /**
     * EventBus instance.
     *
     * @return event bus
     */
    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }
}
