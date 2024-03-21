package com.healthyrecipes.common.utils;


import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class  RedisUtil {

    @Resource
    RedisTemplate<String, Object> redisTemplate;


    /**
     * @description: 获得当前时间到午夜十二点的秒数
     * @param: []
     * @return: java.lang.Long
     */
    public Long getSecondsToMidnight() {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 获取明天凌晨12点的时间
        LocalDateTime midnightTomorrow = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // 计算两个时间之间的时长
        Duration duration = Duration.between(now, midnightTomorrow);

        // 将时长转换为秒
        return duration.getSeconds();
    }

    /**
     * @description: 获得当前格式化的日期(yyyy-MM-dd)
     * @param: []
     * @return: java.lang.String
     */
    public String nowDate(){
        LocalDate now = LocalDate.now();
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 定义日期格式
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 格式化日期为指定格式
        return currentDate.format(dateFormatter);
    }

    /**
     * setex
     *
     * @param key   key
     * @param value value
     * @param time  过期时间
     */
    public void setex(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * set
     * String类型的set,无过期时间
     *
     * @param key   key
     * @param value value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 批量设置key和value
     *
     * @param map key和value的集合
     */
    public void mset(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 如果key不存在，则设置
     *
     * @param key   key
     * @param value value
     * @return 返回是否成功
     */
    public Boolean setnx(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 批量插入key，如果key不存在的话
     *
     * @param map key和value的集合
     * @return 是否成功
     */
    public Boolean msetnx(Map<String, Object> map) {
        return redisTemplate.opsForValue().multiSetIfAbsent(map);
    }

    /**
     * String类型的get
     *
     * @param key key
     * @return 返回value对应的对象
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除对应key
     *
     * @param key key
     * @return 返回是否删除成功
     */
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     *
     * @param keys key的集合
     * @return 返回删除成功的个数
     */
    public Long del(List<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 给某个key设置过期时间
     *
     * @param key  key
     * @param time 过期时间
     * @return 返回是否设置成功
     */
    public Boolean expire(String key, long time) {
        return redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 返回某个key的过期时间
     *
     * @param key key
     * @return 返回key剩余的过期时间
     */
    public Long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 返回是否存在该key
     *
     * @param key key
     * @return 是否存在该key
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 给key的值加上delta值
     *
     * @param key   key
     * @param delta 参数
     * @return 返回key+delta的值
     */
    public Long incrby(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 给key的值减去delta
     *
     * @param key   key
     * @param delta 参数
     * @return 返回key - delta的值
     */
    public Long decrby(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    //hash类型

    /**
     * set hash类型
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     */
    public void hset(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public boolean pullIfExit(String key,String hashKey,Object value){
        return redisTemplate.opsForHash().putIfAbsent(key,hashKey,value);
    }

    /**
     * set hash类型,并设置过期时间
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     * @param time    过期时间
     * @return 返回是否成功
     */
    public Boolean hset(String key, String hashKey, Object value, long time) {
        hset(key, hashKey, value);
        return expire(key, time);
    }

    /**
     * 批量设置hash
     *
     * @param key  key
     * @param map  hashKey和value的集合
     * @param time 过期时间
     * @return 是否成功
     */
    public Boolean hmset(String key, Map<String, Object> map, long time) {
        redisTemplate.opsForHash().putAll(key, map);
        return expire(key, time);
    }

    /**
     * 获取hash类型的值
     *
     * @param key     key
     * @param hashKey hashKey
     * @return 返回对应的value
     */
    public Object hget(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取key下所有的hash值以及hashKey
     *
     * @param key key
     * @return 返回数据
     */
    public Map<Object, Object> hgetall(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 批量删除
     *
     * @param key     key
     * @param hashKey hashKey数组集合
     */
    public void hdel(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 判断是否存在hashKey
     *
     * @param key     key
     * @param hashKey hashKey
     * @return 是否存在
     */
    public Boolean hexists(String key, Object hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey.toString());
    }

    public void hincrby(String key,String field,int increment){
        redisTemplate.opsForHash().increment(key,field,increment);
    }


    public void sadd(String key,Object... values){
        redisTemplate.opsForSet().add(key,values);
    }

    public Long scard(String key){
        return redisTemplate.opsForSet().size(key);
    }

    public void lpushall(String key,Object[] value){
        redisTemplate.opsForList().leftPushAll(key,value);
    }

    public Object lget(String key,int index){
        return redisTemplate.opsForList().index(key,index);
    }

    public void lset(String key,int index,Object value){
        redisTemplate.opsForList().set(key,index,value);
    }

    public void rpushall(String key,Object[] value){
        redisTemplate.opsForList().rightPushAll(key,value);
    }

    public void rpush(String key,Object value){
        redisTemplate.opsForList().rightPush(key,value);
    }

    public List<Object> lrange(String key, int start, int end){
        return redisTemplate.opsForList().range(key, start, end);
    }

    public Long llen(String key){
        return redisTemplate.opsForList().size(key);
    }

    public boolean sismember(String key,Object value){
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    public void increment_hash_double(String key,Object hashKey,Double value){
        redisTemplate.opsForHash().increment(key,hashKey,value);
    }

    public boolean hashKey(String key,Object hashKey){
        return redisTemplate.opsForHash().hasKey(key,hashKey);
    }

    public void hrmKek(String key,Object hashKey){
        redisTemplate.opsForHash().delete(key,hashKey.toString());
    }

    public void srem(String key,Object values) {
        redisTemplate.opsForSet().remove(key,values);
    }

    public Set<Object> smembers(String key){
         return redisTemplate.opsForSet().members(key);
    }

    public void setbitmap(String key,long offset,boolean value){
        redisTemplate.opsForValue().setBit(key,offset,value);
    }

    public Boolean getbitmap(String key,long index){
        return redisTemplate.opsForValue().getBit(key,index);
    }

    public Integer bitcount(String key){
        return Objects.requireNonNull(redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()))).intValue();
    }
}
