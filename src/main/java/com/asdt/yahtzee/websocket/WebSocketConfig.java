package com.asdt.yahtzee.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gamews").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
        /**
         * looks like there is a built in striped executor, so just enable it:
         */
        registry.setPreservePublishOrder(true);
    }

    /**
     * It's Spring web socket design problem. To receive messages in valid order you
     * have to set corePoolSize of websocket clients to 1.
     * 
     * https://stackoverflow.com/questions/29689838/sockjs-receive-stomp-messages-from-spring-websocket-out-of-order
     */
//     @Override
//     public void configureClientOutboundChannel(ChannelRegistration registration) {
//         registration.taskExecutor().corePoolSize(1);
//     }

//     @Override
//     public void configureClientInboundChannel(ChannelRegistration registration) {
//         registration.taskExecutor().corePoolSize(1);
//     }
}