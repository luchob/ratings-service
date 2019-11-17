package com.java2days.ratings.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.
                setApplicationDestinationPrefixes("/app").
                enableSimpleBroker("/ratings")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/socket")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
