package com.jinhx.java.Listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * RedisListener
 *
 * @author jinhx
 * @since 2021-12-31
 */
@Slf4j
public class RedisListener extends KeyExpirationEventMessageListener {

    public RedisListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    protected void doHandleMessage(Message message) {
        Message defaultMessage = new DefaultMessage(message.getChannel(), message.getBody());
        // 获取到key
        String key = defaultMessage.toString();
        log.info("act=RedisListener key={} body={} channel={}", key, defaultMessage.getBody(), defaultMessage.getChannel());
        super.doHandleMessage(message);
    }

}