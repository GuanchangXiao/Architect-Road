package com.springcloud.ribbon.rules;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by perl on 2020-03-14.
 * 自定义Rule 实现一致性哈希算法
 */
@NoArgsConstructor
public class MyRule extends AbstractLoadBalancerRule implements IRule {
    @Override
    public Server choose(Object o) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 获取请求uri
        String uri = request.getServletPath() + "?" + request.getQueryString();

        // 通过uri和serverList计算最后请求的具体服务地址
        return route(uri.hashCode(), getLoadBalancer().getReachableServers());
    }

    private Server route(int hash, List<Server> allServerList) {
        if (CollectionUtils.isEmpty(allServerList)) {
            return null;
        }

        TreeMap<Long, Server> serverMap = new TreeMap<>();

        allServerList.stream().forEach(server -> {
            for (int i = 0; i < 8;i++){
                long key = getHashValue(server.getId() + i);
                serverMap.put(key, server);
            }
        });

        long hashKey = getHashValue(String.valueOf(hash));
        SortedMap<Long, Server> latestServers = serverMap.tailMap(hashKey);

        if (CollectionUtils.isEmpty(latestServers)) {
            return serverMap.firstEntry().getValue();
        }

        // 返回最近的一个节点
        return latestServers.get(latestServers.firstKey());
    }

    private long getHashValue(String key) {
        MessageDigest md5;

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] bytes = null;
        try {
            bytes = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        md5.update(bytes);
        byte[] digest = md5.digest();

        long hashCode = (digest[2] & 0xff << 16) |
                (digest[1] & 0xff << 8) |
                (digest[0] & 0xff);

        return hashCode & 0xffffffffL;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }
}
