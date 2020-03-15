package com.springcloud.hystrix.fallback;

import com.springcloud.feign.client.intf.IService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by perl on 2020-03-15.
 */
@FeignClient(name = "feign-client", fallback = MyFallback.class)
public interface MyService extends IService {


}
