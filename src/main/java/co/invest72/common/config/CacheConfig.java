package co.invest72.common.config;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		cacheManager.setCaches(Arrays.asList(
			buildCache("userMe", Duration.ofMinutes(5), 200),
			buildCache("productSummary", Duration.ofMinutes(30), 100),
			buildCache("productDetail", Duration.ofMinutes(30), 100),
			buildCache("productCalculate", Duration.ofMinutes(30), 100)
		));
		return cacheManager;
	}

	private CaffeineCache buildCache(String name, Duration ttl, int size) {
		return new CaffeineCache(name, Caffeine.newBuilder()
			.expireAfterWrite(ttl)
			.maximumSize(size)
			.build());

	}
}
