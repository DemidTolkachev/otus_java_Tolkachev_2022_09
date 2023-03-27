package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCache<K, V> implements HwCache<K, V> {
    //Надо реализовать эти методы
    private final WeakHashMap<K, V> cashe = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    @Override
    public void put(K key, V value) {
        sendAction(key, value, "put into cache");
        cashe.put(key, value);
    }

    @Override
    public void remove(K key) {
        sendAction(key, cashe.get(key), "remove from cache");
        cashe.remove(key);
    }

    @Override
    public V get(K key) {
        var result = cashe.get(key);
        sendAction(key, result, "get from cache");
        return result;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    @Override
    public void sendAction(K key, V value, String action) {
        listeners.forEach(listener -> {
            try {
                listener.notify(key, value, action);
            } catch (Exception ex) {
                logger.info("Error: {}", ex.getMessage());
            }
        });
    }
}
