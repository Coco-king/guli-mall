package top.codecrab.gulimall.product.web.controller;

import cn.hutool.core.lang.UUID;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author codecrab
 * @since 2021年06月14日 12:16
 */
@RestController
public class TestController {

    @Resource
    private RedissonClient redisson;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 性能压测简单测试
     */
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    /**
     * 测试Redisson可重用锁
     */
    @GetMapping("/lock")
    public String lock() {
        //获取锁，指定名字，名字相同就是同一把锁
        RLock lock = redisson.getLock("my-lock");
        //加锁，阻塞式
        //1、如果业务逻辑未完成会自动续期，默认都是30秒
        //2、业务逻辑完成后就不会自动续期，如果没有手动释放锁（服务器宕机）那么会在默认30秒后删除
        //默认续期采用看门狗机制,RedissonLock的174行源码，默认续期时间是30秒，有一个定时任务，业务执行期间每30/3秒执行一次续期
        //lock.lock();
        //如果自己指定过期时间，那么就不会触发看门狗自动续期，建议使用这种，设置30秒（看门狗默认）超时时间，因为30秒还未执行完成，业务肯定挂了
        lock.lock(30, TimeUnit.SECONDS);
        try {
            System.out.println("加锁成功，执行业务逻辑，线程ID：" + Thread.currentThread().getId());
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println("释放锁，线程ID：" + Thread.currentThread().getId());
        }
        return "redisson lock";
    }

    @GetMapping("/write")
    public String write() {
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        String v = "";
        //写锁，如果写锁没有释放，那么所有读锁就必须等待，写锁是一个排它锁（互斥锁），读锁是一个共享锁
        //读 + 读：不用等待，会在redis中记录并发读的线程
        //读 + 写：写锁需要等待读锁释放
        //写 + 写：需要等待写锁释放
        //写 + 读：读锁需要等待写锁释放，并发进来的读会在redis中记录线程
        RLock rLock = lock.writeLock();
        try {
            rLock.lock();
            v = UUID.fastUUID().toString();
            TimeUnit.SECONDS.sleep(30);
            redisTemplate.opsForValue().set("writeValue", v);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return v;
    }

    @GetMapping("/read")
    public String read() {
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        //读锁
        RLock rLock = lock.readLock();
        Object value = null;
        try {
            rLock.lock();
            value = redisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return value == null ? "1111" : value.toString();
    }

}
