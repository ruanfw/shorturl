package com.yunbei.shorturl.core.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.yunbei.shorturl.core.cache.RedisCache;
import com.yunbei.shorturl.core.event.handler.IEventHandler;

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {

	private static final Logger LOG = LoggerFactory.getLogger(EventConsumer.class);

	private static final int THREAD_NUM = 1;

	private static ApplicationContext applicationContext;
	private static Map<EventType, List<IEventHandler>> handlers = new HashMap<EventType, List<IEventHandler>>();

	@Autowired
	private RedisCache redisCache;

	@Override
	public void afterPropertiesSet() throws Exception {

		boolean result = initHandlers();

		if (!result) {
			return;
		}

		// test();

		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);

		for (int i = 0; i < THREAD_NUM; i++) {
			executorService.submit(new ConsumerThread());
		}

	}

	// private void test() {
	//
	// String key = redisCache.genKey(Event.class);
	// // brpop 如果redis里无事件，则会阻塞
	// // Event event = redisCache.brpopOnlyObj(0, key, Event.class);
	// Event event = redisCache.rpopObj(key, Event.class);
	//
	// if (event == null) {
	// LOG.warn("event is null");
	// return;
	// }
	//
	// LOG.warn("event:{}", event.toString());
	//
	// if (!handlers.containsKey(event.getEventType())) {
	// LOG.warn("handler not has this type:{} ", event.getEventType());
	// return;
	// }
	//
	// for (IEventHandler eventHandler : handlers.get(event.getEventType())) {
	// eventHandler.deal(event);
	// }
	//
	// }

	private class ConsumerThread implements Runnable {

		@Override
		public void run() {

			while (true) {

				String key = redisCache.genKey(Event.class);
				// brpop 如果redis里无事件，则会阻塞
				Event event = redisCache.brpopOnlyObj(0, key, Event.class);

				LOG.warn("event:{}", event.toString());

				if (!handlers.containsKey(event.getEventType())) {
					LOG.warn("handler not has this type:{} ", event.getEventType());
					continue;
				}

				for (IEventHandler eventHandler : handlers.get(event.getEventType())) {
					eventHandler.deal(event);
				}

			}

		}

	}

	private boolean initHandlers() {

		Map<String, IEventHandler> beans = applicationContext.getBeansOfType(IEventHandler.class);

		if (beans == null || beans.size() <= 0) {
			LOG.warn("beans of IEventHandler is null");
			return false;
		}

		for (Entry<String, IEventHandler> bean : beans.entrySet()) {

			List<EventType> eventTypes = bean.getValue().getHandleEventTypes();

			for (EventType eventType : eventTypes) {

				if (!handlers.containsKey(eventType)) {
					handlers.put(eventType, new ArrayList<IEventHandler>());
				}

				handlers.get(eventType).add(bean.getValue());
			}
		}

		return true;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
