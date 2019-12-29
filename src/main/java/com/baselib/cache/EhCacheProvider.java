package com.baselib.cache;

import java.util.Map;

public class EhCacheProvider { //implements CacheProvider {
/*
        private CacheManager cacheManager;
        private String cacheRegionName;
        private Map<String, Long> cacheHits = new HashMap<String, Long>();
        private Map<String, Long> cacheMisses = new HashMap<String, Long>();
        
        public EhCacheProvider(String cacheRegionName) {
                this.cacheRegionName = cacheRegionName;
        }

        private CacheManager getCacheManager(){
                if (cacheManager == null)
                        cacheManager = (CacheManager) CacheManager.ALL_CACHE_MANAGERS.get(0);
                return cacheManager;
        }
                        
        public void clearCache() {
                getCacheManager().clearAll();
                cacheHits.clear();
                cacheMisses.clear();
        }

        public String getCacheRegionName() {
                return cacheRegionName;
        }

        public Long getCacheHits(String key) {
                Long hits = cacheHits.get(key);
                if (hits == null) {
                        hits = 0L;
                        cacheHits.put(key, hits);
                }
                return hits;
        }       

        public Long getCacheMisses(String key) {
                Long misses = cacheMisses.get(key);
                if (misses == null) {
                        misses = 0L;
                        cacheMisses.put(key, misses);
                }
                return misses;
        }

        public Object getResultFromCache(String key, Map values) {
                Cache cache  = getCacheManager().getCache(getCacheRegionName());
                if (cache == null) {
                        addCacheMiss(key);
                        return null;
                }
                String key = createResultKey(key, values);
                Element element = cache.get(key);
                if (element == null) {
                        addCacheMiss(key);                  
                        return null;
                }
                addCacheHit(key);
                return element.getObjectValue();
        }

        public void putResultToCache(String key, Map values, Object result) {
                Cache cache  = getCacheManager().getCache(getCacheRegionName());
                if (cache == null) return;
                String key = createResultKey(key, values);          
                cache.put(new Element(key, result));
        }
        
        private void addCacheHit(String key) {

                synchronized (cacheHits) {
                
                        Long hits = cacheHits.get(key);
                        
                        if (hits == null) hits = 0L;
                        	cacheHits.put(key, ++hits);
                }
        }

        private void addCacheMiss(String key) {
                synchronized (cacheMisses) {
                
                        Long misses = cacheMisses.get(key);
                        
                        if (misses == null) misses = 0L;
                        	cacheMisses.put(key, ++misses);
                }
        }
*/
        private String createResultKey(String key, Map values) {
        	
                StringBuffer sb = new StringBuffer();
                sb.append(key + ":");
            
                for (Object keys : values.keySet()) {
                	
                     sb.append("|" + keys + ":" + values.get(keys) + "|");
                }
                
                return sb.toString();
        }       

}