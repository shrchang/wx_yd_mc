package com.booking.wechat.persistence.service.base;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 实体操作通用接口
 * 
 * @author Luozhh,Time 2013-1-17
 * @param <T>
 */
public interface DAO<T> {

    /**
     * 根据实体模糊查询
     * 
     * @param entity
     * @return
     */
    public QueryResult<T> findByExample(T entity);

    /**
     * <pre>
     * 根据实体查询
     * @param entity 实体实例
     * @param isExact 是否准确匹配，false : 否，即模糊查询；true ： 是
     * @return
     * </pre>
     */
    public QueryResult<T> findByExample(T entity, boolean isExact);

    /**
     * <pre>
     * 根据实体分页查询
     * @param entity 实体实例
     * @param isExact 是否准确匹配，false : 否，即模糊查询；true ： 是
     * @param firstResult 开始索引,以0开始,如果输入值为-1,即获取全部数据
     * @param maxResult 每页获取的记录数,如果输入值为-1,即获取全部数据
     * @return
     * </pre>
     */
    public QueryResult<T> findByExample(T entity, boolean isExact, int firstResult, int maxResult);

    /**
     * <pre>
     * 根据实体分页查询查询
     * @param entity 实体实例
     * @param isExact 是否准确匹配，false : 否，即模糊查询；true ： 是
     * @param firstResult 开始索引,以0开始,如果输入值为-1,即获取全部数据
     * @param maxResult 每页获取的记录数,如果输入值为-1,即获取全部数据
     * @param orderby
     *            排序,Key为排序属性,Value为asc/desc,如:
     *            LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
     *            orderby.put("email", "asc");
     *            orderby.put("password", "desc");
     * @return
     * </pre>
     */
    public QueryResult<T> findByExample(T entity, boolean isExact, int firstResult, int maxResult, LinkedHashMap<String, String> orderby);

    /**
     * <pre>
     * @param params
     * @param isExact 是否准确匹配，false : 否，即模糊查询；true ： 是
     * @return
     * </pre>
     */
    public QueryResult<T> findByExample(Map<String, Object> params, boolean isExact);

    /**
     * <pre>
     * @param params
     * @param isExact 是否准确匹配，false : 否，即模糊查询；true ： 是
     * @param orderby 
     *            每页获取的记录数,如果输入值为-1,即获取全部数据
     *            LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
     *            orderby.put("email", "asc");
     *            orderby.put("password", "desc");
     * @return
     * </pre>
     */
    public QueryResult<T> findByExample(Map<String, Object> params, boolean isExact, LinkedHashMap<String, String> orderby);

    /**
     * <pre>
     * @param params
     * @param isExact 是否准确匹配，false : 否，即模糊查询；true ： 是
     * @param firstResult 开始索引,以0开始,如果输入值为-1,即获取全部数据
     * @param maxResult
     *            每页获取的记录数,如果输入值为-1,即获取全部数据
     *            LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
     *            orderby.put("email", "asc");
     *            orderby.put("password", "desc");
     * @return
     * </pre>
     */
    public QueryResult<T> findByExample(Map<String, Object> params, boolean isExact, int firstResult, int maxResult,
        LinkedHashMap<String, String> orderby);

    /**
     * <pre>
     * 分页查询
     * @param params
     * @param firstResult
     * @param maxResult
     * @return
     * </pre>
     */
    public QueryResult<T> findByExampleForPage(Map<String, Object> params, int firstResult, int maxResult);

    /**
     * 分页查询
     * 
     * @param params
     * @param isExact
     * @param firstResult
     * @param maxResult
     * @param orderby
     * @param isNullOrNot
     * @return
     */
    public QueryResult<T> findByExample(Map<String, Object> params, boolean isExact, int firstResult, int maxResult,
        LinkedHashMap<String, String> orderby, Map<String, Boolean> isNullOrNot);

    /**
     * <pre>
     * 保存实体
     * @param entity 实体对象
     * </pre>
     */
    public void save(T entity);

    /**
     * 清除一级缓存的数据
     */
    public void clear();

    /**
     * <pre>
     * 更新实体
     * @param entity  实体对象
     * </pre>
     */
    public void update(T entity);

    /**
     * <pre>
     * 删除实体
     * @param entityId 实体标识
     * </pre>
     */
    public void delete(Serializable... entityIds);// JPA 规定实体的id属性必须实现序列化接口

    /**
     * <pre>
     * 删除实体
     * @param entity  实体对象
     * </pre>
     */
    public void remove(T entity);

    /**
     * <pre>
     * 根据实体标识查找实体
     * @param entityId 实体标识
     * @return 实体对象
     * </pre>
     */
    public T find(Serializable entityId);

    /**
     * <pre>
     * Description:查询实体所有记录
     * @return
     * </pre>
     */
    public QueryResult<T> getScrollData();

    /**
     * <pre>
     * 分页查询实体记录
     * @param firstResult 开始索引,以0开始,如果输入值为-1,即获取全部数据
     * @param maxResult 每页获取的记录数,如果输入值为-1,即获取全部数据
     * @return
     * </pre>
     */
    public QueryResult<T> getScrollData(int firstResult, int maxResult);

    /**
     * <pre>
     * 按条件查询实体记录
     * @param where 构建jpql where语句部分，如"o.id = ?1 and o.name=?2"
     * @param params
     * @return
     * </pre>
     */
    public QueryResult<T> getScrollData(String where, Object[] params);

    /**
     * <pre>
     * 分页查询实体记录
     * @param firstResult 开始索引,以0开始,如果输入值为-1,即获取全部数据
     * @param maxResult 每页获取的记录数,如果输入值为-1,即获取全部数据
     * @param orderby
     *            排序,Key为排序属性,Value为asc/desc,如:
     *            LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
     *            orderby.put("email", "asc");
     *            orderby.put("password", "desc");
     * @return
     * </pre>
     */
    public QueryResult<T> getScrollData(int firstResult, int maxResult, LinkedHashMap<String, String> orderby);

    /**
     * <pre>
     * 分页查询实体记录
     * @param firstResult 开始索引,以0开始,如果输入值为-1,即获取全部数据
     * @param maxResult  每页获取的记录数,如果输入值为-1,即获取全部数据
     * @param where 条件语句,不带where关键字,条件语句只能使用位置参数,位置参数的索引值以1开始,如:o.username=?1 and o.password=?2
     * @param params 条件语句出现的位置参数值
     * @return
     * </pre>
     */
    public QueryResult<T> getScrollData(int firstResult, int maxResult, String where, Object[] params);

    /**
     * <pre>
     * 分页查询实体记录
     * @param firstResult 开始索引,以0开始,如果输入值为-1,即获取全部数据
     * @param maxResult 每页获取的记录数,如果输入值为-1,即获取全部数据
     * @param where 条件语句,不带where关键字,条件语句只能使用位置参数,位置参数的索引值以1开始,如:o.username=?1 and o.password=?2
     * @param params 条件语句出现的位置参数值
     * @param orderby
     *            排序,Key为排序属性,Value为asc/desc,如:
     *            LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
     *            orderby.put("email", "asc");
     *            orderby.put("password", "desc");
     * @return
     * </pre>
     */
    public QueryResult<T> getScrollData(int firstResult, int maxResult, String where, Object[] params, LinkedHashMap<String, String> orderby);

    /**
     * 获取所有的数据
     * 
     * @return
     */
    public List<T> getAll();

    /**
     * 获取实体的总记录数
     * 
     * @return 总记录数
     */
    public long getCount();

    /**
     * 按条件获取实体记录数
     * 
     * @param where
     * @param params
     * @return
     */
    public long getCount(String where, Object[] params);

    /**
     * 模糊查询
     * 
     * @param params
     * @return
     */
    public QueryResult<T> findByExample(Map<String, Object> params);

    /**
     * 不分页的模糊查询
     * 
     * @param params
     * @return
     */
    public List<T> findByExampleNoPage(Map<String, Object> params);

    /**
     * 批量修改
     * 
     * @param jpql
     * @return
     */
    public int batchUpdate(String jpql);

    /**
     * 批量修改
     * 
     * @param jpql
     * @return
     */
    public int batchUpdate(String jpql, Object[] params);

    /**
     * 根据自定义sql查询
     * 
     * @param jpql
     * @return
     */
    public List<Object> finByNativeSql(String jpql);

    public List<T> finByNativeSql(String jpql, Object[] params);

    /**
     * <pre>
     * 按OR的方式查询记录，并分页。
     * @param params
     * @param isExact 是否准确匹配，false : 否，即模糊查询；true ： 是
     * @param firstResult 开始索引,以0开始,如果输入值为-1,即获取全部数据
     * @param maxResult 每页获取的记录数,如果输入值为-1,即获取全部数据
     * @param orderby
     *            排序,Key为排序属性,Value为asc/desc,如:
     *            LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
     *            orderby.put("email", "asc");
     *            orderby.put("password", "desc");
     * @return
     * </pre>
     */
    public QueryResult<T> findByOr(Map<String, Object> params, boolean isExact, int firstResult, int maxResult, LinkedHashMap<String, String> orderby);

    /**
     * <pre>
     * 按OR的方式查询记录，不分页。
     * @param params
     * @param isExact 是否准确匹配，false : 否，即模糊查询；true ： 是
     * @param orderby
     *            排序,Key为排序属性,Value为asc/desc,如:
     *            LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
     *            orderby.put("email", "asc");
     *            orderby.put("password", "desc");
     * @return
     * </pre>
     */
    public QueryResult<T> findByOr(Map<String, Object> params, boolean isExact, LinkedHashMap<String, String> orderby);

    /**
     * 根据自定义sql查询
     * 
     * @param jpql
     * @return
     */
    public List<T> findByCustomJpql(String jpql);

    /**
     * 根据自定义sql查询
     * 
     * @param jpql
     * @return
     */
    public List<T> findByCustomJpql(String jpql, Object[] params);

    /**
     * 自定义sql查询
     * 
     * @param jpql
     * @param firstResult
     * @param maxResult
     * @return
     */
    public List<T> findByCustomJpql(String jpql, Integer firstResult, Integer maxResult);

    /**
     * 统计总数
     * 
     * @param jpql
     * @return
     */
    public Long getCountByCustomSql(String jpql);

    public Long getCountByCustomSql(String jpql, Object[] params);

    /**
     * 批量删除，执行本地sql.不要传入jpql
     * 
     * @param localSql
     * @return
     */
    public int batchDeleteByNativeSql(String localSql);

    QueryResult<T> findByExample(Map<String, Object> params, String groupBy);
}
