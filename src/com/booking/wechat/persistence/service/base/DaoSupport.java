package com.booking.wechat.persistence.service.base;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.StaleStateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.booking.wechat.util.BeanMapConverUtil;
import com.booking.wechat.util.StringUtil;

/**
 * 实体操作通用接口实现基类
 * 
 * @author Luozhh,Time 2013-1-17
 * @param <T>
 */
@SuppressWarnings("unchecked")
@Transactional
public class DaoSupport<T> implements DAO<T> {

    @PersistenceContext
    protected EntityManager     em;

    @Autowired
    protected HibernateTemplate hibernateTemplate;

    protected Logger            logger      = Logger.getLogger(getClass());
    /** 实体类 */
    protected Class<T>          entityClass = GenericsUtils.getSuperClassGenricType(getClass());

    public QueryResult<T> findByExample(T entity) {
        return findByExample(entity, false);
    }

    /**
     * 不分页的方式查询
     * 
     * @param entity
     * @return
     */
    public List<T> findByExampleNoPage(T entity) {
        return hibernateTemplate.findByExample(entity);
    }

    public QueryResult<T> findByExample(T entity, boolean isExact) {
        return findByExample(entity, isExact, -1, -1);
    }

    public QueryResult<T> findByExample(T entity, boolean isExact, int firstResult, int maxResult) {
        return findByExample(entity, isExact, firstResult, maxResult, null);
    }

    public QueryResult<T> findByExample(T entity, boolean isExact, int firstResult, int maxResult, LinkedHashMap<String, String> orderby) {
        Map<String, Object> um = BeanMapConverUtil.beanAndSuperToMap(entity);
        return findByExample(um, isExact, firstResult, maxResult, orderby);
    }

    public QueryResult<T> findByExample(Map<String, Object> params) {
        return findByExample(params, false);
    }

    public QueryResult<T> findByExample(Map<String, Object> params, boolean isExact) {
        return findByExample(params, isExact, null);
    }

    public QueryResult<T> findByExample(Map<String, Object> params, boolean isExact, LinkedHashMap<String, String> orderby) {
        int firstIndex = -1;
        int maxResult = -1;
        if (params.containsKey("firstResult")) {
            firstIndex = Integer.parseInt((String)params.get("firstResult"));
        }
        if (params.containsKey("maxResult")) {
            maxResult = Integer.parseInt((String)params.get("maxResult"));
        }
        return findByExample(params, isExact, firstIndex, maxResult, orderby);
    }

    public List<T> findByExampleNoPage(Map<String, Object> params) {
        return findByExample(params).getRows();
    }

    public QueryResult<T> findByExampleForPage(Map<String, Object> params, int firstResult, int maxResult) {
        return findByExample(params, false, firstResult, maxResult, null);
    }

    public QueryResult<T> findByExample(Map<String, Object> params, boolean isExact, int firstResult, int maxResult,
        LinkedHashMap<String, String> orderby, Map<String, Boolean> isNullOrNot) {
        String jpql = buildJPQL(params, isExact, isNullOrNot);
        String where = null;
        if (jpql.indexOf("and") != -1) {
            where = jpql.substring(jpql.indexOf("and") + 3);
        }
        return getScrollData(firstResult, maxResult, where, null, orderby);
    }

    public QueryResult<T> findByExample(Map<String, Object> params, String groupBy) {
        return findByExample(params, false, -1, -1, null, null, groupBy);
    }

    private QueryResult<T> findByExample(Map<String, Object> params, boolean isExact, int firstResult, int maxResult,
        LinkedHashMap<String, String> orderby, Map<String, Boolean> isNullOrNot, String groupBy) {
        String jpql = buildJPQL(params, isExact, isNullOrNot);
        String where = null;
        if (jpql.indexOf("and") != -1) {
            where = jpql.substring(jpql.indexOf("and") + 3);
        }
        if (where == null) {
            where = " group by o." + groupBy;
        }
        else {
            where += " group by o." + groupBy;
        }
        return getScrollData(firstResult, maxResult, where, null, orderby);
    }

    public QueryResult<T> findByExample(Map<String, Object> params, boolean isExact, int firstResult, int maxResult,
        LinkedHashMap<String, String> orderby) {
        return findByExample(params, isExact, firstResult, maxResult, orderby, null);
    }

    public QueryResult<T> findByOr(Map<String, Object> params, boolean isExact, int firstResult, int maxResult, LinkedHashMap<String, String> orderby) {
        String where = buildWhereWithOr(params, isExact);// 当前实现不规范,会有sql注入的风险。
        return getScrollData(firstResult, maxResult, where, null, orderby);
    }

    public QueryResult<T> findByOr(Map<String, Object> params, boolean isExact, LinkedHashMap<String, String> orderby) {
        String where = buildWhereWithOr(params, isExact);// 当前实现不规范,会有sql注入的风险。
        return getScrollData(-1, -1, where, null, orderby);
    }

    /**
     * 构建查询where部分 ，形如 where 1=1 ,或 where o.a=1 or o.b=2。
     * 
     * @param params
     * @param isExact
     * @return
     */
    protected String buildWhereWithOr(Map<String, Object> params, boolean isExact) {

        StringBuilder conditionsBuilder = new StringBuilder();
        List<Field> fileds = getUnTransientFields();
        for (Map.Entry<String, Object> key : params.entrySet()) {
            for (int i = 0; i < fileds.size(); i++) {
                Field filed = fileds.get(i);
                String filedName = filed.getName();
                if (key.getKey().equalsIgnoreCase(filedName)) {
                    conditionsBuilder.append(" or o.");
                    conditionsBuilder.append(filedName);
                    if (isExact) {
                        conditionsBuilder.append(getFileTypeByExact(filed, key.getValue()));
                    }
                    else {
                        conditionsBuilder.append(getFileTypeByDim(filed, key.getValue()));
                    }
                }
            }
        }
        String condtions = conditionsBuilder.toString();
        if (StringUtil.isNull(condtions)) {
            return "1=1";
        }
        else {
            return condtions.replaceFirst("or", "");
        }

    }

    public List<Object> finByNativeSql(String jpql) {
        Query query = em.createQuery(jpql);
        return query.getResultList();
    }

    /**
     * 使用本地sql
     * 
     * @param sql
     * @return
     */
    public List<Object[]> queryByNativeSql(String sql) {
        Query query = em.createNativeQuery(sql);
        return query.getResultList();
    }
    
    /**
     * 希望可以吧
     * @author shrChang.Liu
     * @param sql
     * @return
     * @date 2018年10月29日 下午4:21:52
     * @return List<Map>
     * @description
     */
    @SuppressWarnings("rawtypes")
	public List<Map> queryByNativeSQL(String sql){
    	 Query query = em.createNativeQuery(sql);
    	 query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    	 return query.getResultList();
    }

    public void execUpdateSql(String jpql) {
        Query query = em.createQuery(jpql);
        query.executeUpdate();
    }

    public QueryResult<T> getScrollData() {
        return getScrollData(-1, -1);
    }

    public QueryResult<T> getScrollData(int firstResult, int maxResult) {
        return getScrollData(firstResult, maxResult, null, null, null);
    }

    public QueryResult<T> getScrollData(String where, Object[] params) {
        return getScrollData(-1, -1, where, params);
    }

    public QueryResult<T> getScrollData(int firstResult, int maxResult, LinkedHashMap<String, String> orderby) {
        return getScrollData(firstResult, maxResult, null, null, orderby);
    }

    public QueryResult<T> getScrollData(int firstResult, int maxResult, String where, Object[] params) {
        return getScrollData(firstResult, maxResult, where, params, null);
    }

    public QueryResult<T> getScrollData(int firstResult, int maxResult, String where, Object[] params, LinkedHashMap<String, String> orderby) {
        QueryResult<T> resultList = new QueryResult<T>();
        String entityname = getEntityName(this.entityClass);
        String wherejpql = (where != null && !"".equals(where)) ? " where " + where : "";
        String jpql = "select o from " + entityname + " o" + wherejpql + buildOrderby(orderby);

        Query query = em.createQuery(jpql);
        if (firstResult != -1 && maxResult != -1) {
        	firstResult--;
        	if(firstResult!=0){
        		firstResult = firstResult*maxResult;
        	}
        	query.setFirstResult(firstResult).setMaxResults(maxResult);
        }
        setQueryParameter(query, params);
        resultList.setRows(query.getResultList());

        query = em.createQuery("select count(" + getCountField(entityClass) + ") from " + entityname + " o" + wherejpql);
        setQueryParameter(query, params);
        // Object o = query.getSingleResult();
        try {
            resultList.setTotal((Long)query.getSingleResult());
        }
        catch (NoResultException e) {
            resultList.setTotal(0l);
        }

        return resultList;
    }

    /**
     * 统计总数
     * 
     * @param jpql
     * @return
     */
    public Long getCountByCustomSql(String jpql) {
        return getCountByCustomSql(jpql, null);
    }

    public Long getCountByCustomSql(String jpql, Object[] params) {
        Query query = em.createQuery(jpql);
        setQueryParameter(query, params);
        return (Long)query.getSingleResult();
    }

    public List<T> getAll() {
        return getScrollData().getRows();
    }

    public List<T> findByCustomJpql(String jpql) {
        return findByCustomJpql(jpql, null);
    }

    public List<T> findByCustomJpql(String jpql, Object[] params) {
        return finByNativeSql(jpql, params);
    }

    /**
     * 自定义sql查询
     * 
     * @param jpql
     * @param firstResult
     * @param maxResult
     * @return
     */
    public List<T> findByCustomJpql(String jpql, Integer firstResult, Integer maxResult) {
        Query query = em.createQuery(jpql);
        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }
        if (maxResult != null) {
            query.setMaxResults(maxResult);
        }
        return query.getResultList();
    }

    public List<T> finByNativeSql(String jpql, Object[] params) {
        try {
            Query query = em.createQuery(jpql);
            setQueryParameter(query, params);
            return query.getResultList();
        }
        catch (StaleStateException e) {
            logger.error("查询异常：" + jpql, e);
            // 查询居然也会报这个问题，估计是查询前会自动flush一次
            // 具体的错误是：org.hibernate.StaleStateException: Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1
            return new ArrayList<T>();
        }
        catch (OptimisticLockException e) {
            logger.error("查询异常：" + jpql, e);
            return new ArrayList<T>();
        }
    }

    /**
     * <p>
     * 设置查询参数
     * <p>
     * 
     * @param query
     *            查询对象
     * @param params
     *            参数值
     */
    public void setQueryParameter(Query query, Object[] params) {
        if (params != null && params.length > 0) for (int i = 0; i < params.length; i++)
            query.setParameter(i + 1, params[i]);
    }

    /**
     * 构造排序语句
     * 
     * @param orderby
     *            排序属性与asc/desc,Key为排序属性,Value为asc/desc
     * @return 排序语句
     */
    public String buildOrderby(LinkedHashMap<String, String> orderby) {
        StringBuilder orderbySql = new StringBuilder("");
        if (orderby != null && !orderby.isEmpty()) {
            orderbySql.append(" order by ");
            for (Entry<String, String> entry : orderby.entrySet()) {
                orderbySql.append("o.").append(entry.getKey()).append(" ").append(entry.getValue()).append(",");
            }
            orderbySql.deleteCharAt(orderbySql.length() - 1);
        }
        return orderbySql.toString();
    }

    public void save(T entity) {
        em.persist(entity);
    }

    public void clear() {
        em.clear();
    }

    public void update(T entity) {
        em.merge(entity);
    }

    public void remove(T entity) {
        em.remove(entity);
    }

    public int batchUpdate(String jpql) {
        return em.createQuery(jpql).executeUpdate();
    }

    public int batchUpdate(String jpql, Object[] params) {
        Query query = em.createQuery(jpql);
        setQueryParameter(query, params);
        return query.executeUpdate();
    }

    public void delete(Serializable... entityIds) {
        for (Serializable entityId : entityIds) {

            try {

                em.remove(em.getReference(entityClass, entityId));

            }
            catch (EntityNotFoundException e) {
                logger.error("主键ID[" + entityId + "]没找到", e);
            }
            catch (StaleStateException e) {
                logger.error("主键ID[" + entityId + "]没找到", e);
            }// 删除异常不抛出 chenmy
            catch (OptimisticLockException e) {
                logger.error("主键ID[" + entityId + "]没找到", e);
            }

        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public T find(Serializable entityId) {
        return em.find(entityClass, entityId);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public long getCount() {
        return (Long)em.createQuery(getCountQuery()).getSingleResult();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public long getCount(String where, Object[] params) {
        String wherejpql = (where != null && !"".equals(where)) ? getCountQuery() + " where " + where : "";
        Query query = em.createQuery(wherejpql);
        setQueryParameter(query, params);
        return (Long)query.getSingleResult();
    }

    private String getCountQuery() {
        return "select count(" + getCountField(entityClass) + ") from " + getEntityName(entityClass) + " o ";
    }

    /**
     * 获取实体名称
     * 
     * @return
     */
    protected <E> String getEntityName(Class<E> entityClass) {
        String entityName = entityClass.getSimpleName();
        Entity entity = entityClass.getAnnotation(Entity.class);
        if (entity != null) if (entity.name() != null && !"".equals(entity.name())) entityName = entity.name();
        return entityName;
    }

    /**
     * 获取统计属性,该方法是为了解决Hibernate解析联合主键select count(o) from Xxx o语句BUG增加,<br>
     * Hibernate对此jpql解析后的sql为select count(field1,field2,...),显示使用count()统计多个字段是错误的
     * 
     * @param <E>
     * @param clazz
     * @return
     */
    protected static <E> String getCountField(Class<E> clazz) {
        String out = "o";
        try {
            PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
            for (PropertyDescriptor propertyDesc : propertyDescriptors) {
                Method method = propertyDesc.getReadMethod();
                if (method != null && method.isAnnotationPresent(EmbeddedId.class)) {
                    PropertyDescriptor[] ps = Introspector.getBeanInfo(propertyDesc.getPropertyType()).getPropertyDescriptors();
                    out = "o." + propertyDesc.getName() + "." + (!ps[1].getName().equals("class") ? ps[1].getName() : ps[0].getName());
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    protected String buildJPQL(Map<String, Object> params, boolean isExact, Map<String, Boolean> isNullOrNot) {
        StringBuilder jpql = new StringBuilder(buildJPQL(params, isExact));
        if (isNullOrNot != null && !isNullOrNot.isEmpty()) {
            for (Entry<String, Boolean> en : isNullOrNot.entrySet()) {
                String filedName = en.getKey();
                jpql.append(" and o.").append(filedName);
                if (en.getValue()) {
                    jpql.append(" is null ");
                }
                else {
                    jpql.append(" is not null ");
                }
            }
        }
        return jpql.toString();
    }

    /**
     * 构建JPQL语句
     * 
     * @param params
     * @param isExact
     * @return
     */
    protected String buildJPQL(Map<String, Object> params, boolean isExact) {
        StringBuilder conditionsBuilder = new StringBuilder();
        conditionsBuilder.append("  select o from  ");
        conditionsBuilder.append(getEntityName(this.entityClass)).append(" o ");
        conditionsBuilder.append(" where 1=1  ");
        List<Field> fileds = getUnTransientFields();
        for (Map.Entry<String, Object> key : params.entrySet()) {
            for (int i = 0; i < fileds.size(); i++) {
                Field filed = fileds.get(i);
                String filedName = filed.getName();
                if (key.getKey().equalsIgnoreCase(filedName)) {
                    conditionsBuilder.append(" and o.");
                    conditionsBuilder.append(filedName);
                    if (key.getValue() instanceof List) {
                        conditionsBuilder.append(getFileTypeByList(filed, (List<Object>)key.getValue()));
                    }
                    else if (isExact) {
                        conditionsBuilder.append(getFileTypeByExact(filed, escape(key.getValue())));
                    }
                    else {
                        conditionsBuilder.append(getFileTypeByDim(filed, escape(key.getValue())));
                    }
                }
            }
        }

        // or查询:key为search的标识为【或】条件查询 Luoxx：key:search,value:Map<filed,value>
        if (params.containsKey("search")) {
            if (params.get("search") instanceof Map) {
                Map<String, Object> searchs = (Map<String, Object>)params.get("search");
                conditionsBuilder.append(" and (");
                int index = 0;
                for (Map.Entry<String, Object> key : searchs.entrySet()) {
                    for (int i = 0; i < fileds.size(); i++) {
                        Field filed = fileds.get(i);
                        String filedName = filed.getName();
                        if (key.getKey().equalsIgnoreCase(filedName)) {
                            if (index == 0) {
                                conditionsBuilder.append(" o.");
                            }
                            else {
                                conditionsBuilder.append(" or o.");
                            }
                            conditionsBuilder.append(filedName);
                            conditionsBuilder.append(getFileTypeByDim(filed, escape(key.getValue())));
                            index++;
                        }
                    }
                }
                conditionsBuilder.append(" )");
            }
        }
        return conditionsBuilder.toString();
    }

    /**
     * <pre>
     * 转义,只对字符串做转义处理
     * 1、将\转换为\\
     * @return
     * </pre>
     */
    private Object escape(Object obj) {
        if (obj instanceof String) {
            String value = (String)obj;
            if (value.contains("<>")) {
                return obj;
            }
            if (value.contains("\\")) {
                return value.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\\", "\\\\\\\\");
            }
            if (value.indexOf("'") != -1) {
                return value.replaceAll("'", "\\\\'");
            }
        }
        return obj;
    }

    protected List<Field> getUnTransientFields() {
        // 所有非瞬时(@Transient)字段
        List<Field> fields = getAllFields();
        // 所有非瞬时(@Transient)get方法
        List<Method> methods = getAllMethods();
        // 综合字段及get方法得出最终字段（有些方法有瞬时标签，有些是字段）
        List<Field> unTranFields = new ArrayList<Field>();
        for (Field field : fields) {
            String prefixMethodName = field.getName().substring(0, 1).toUpperCase();
            String suffixMethodName = field.getName().substring(1);
            String fieldGetName = "get".concat(prefixMethodName).concat(suffixMethodName);
            for (Method method : methods) {
                if (fieldGetName.equals(method.getName())) {
                    if (!isTransient(field) && !isTransient(method)) {
                        unTranFields.add(field);
                    }
                }
            }
        }
        return unTranFields;
    }

    protected List<Field> getAllFields() {
        List<Field> unTransientFields = new ArrayList<Field>();
        Class<? extends Object> type = entityClass;
        for (; type != Object.class; type = type.getSuperclass()) {
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                Annotation tran = field.getAnnotation(Transient.class);
                if (tran == null) {
                    unTransientFields.add(field);
                }
            }
        }
        return unTransientFields;
    }

    private List<Method> getAllMethods() {
        List<Method> unTransientMethods = new ArrayList<Method>();
        Class<? extends Object> type = entityClass;
        for (; type != Object.class; type = type.getSuperclass()) {
            Method[] methods = type.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().indexOf("get") == 0) {
                    unTransientMethods.add(method);
                }
            }
        }
        return unTransientMethods;
    }

    private boolean isTransient(Field field) {
        return field.getAnnotations() != null && field.getAnnotations().length > 0
            && field.getAnnotations()[0].annotationType() == javax.persistence.Transient.class;
    }

    private boolean isTransient(Method method) {
        return method.getAnnotations() != null && method.getAnnotations().length > 0
            && method.getAnnotations()[0].annotationType() == javax.persistence.Transient.class;
    }

    protected String getFileTypeByDim(Field filed, Object value) {
        StringBuffer sql = new StringBuffer();
        Class<?> fileType = filed.getType();
        if (fileType == String.class) {
            try {
                String strValue = String.valueOf(value);
                if (strValue.trim().startsWith("<>")) {
                    sql.append(value);
                    return sql.toString();
                }
            }
            catch (Exception e) {
            }

            sql.append(" like '%");
            sql.append(value);
            sql.append("%' ");
        }
        else if (fileType == Long.class && (value + "").contains(",")) {
            sql.append(" in (");
            sql.append(value);
            sql.append(")");
        }
        else if (fileType.isEnum()) {
            String strValue = String.valueOf(value);
            if (strValue.trim().startsWith("<>")) {
                sql.append(value);
            }
            else {
                sql.append(" = '");
                sql.append(value);
                sql.append("' ");
            }
        }
        else {
            sql.append(" = ");
            sql.append(value);
        }
        return sql.toString();
    }

    protected String getFileTypeByExact(Field filed, Object value) {
        StringBuffer sql = new StringBuffer();
        Class<?> fileType = filed.getType();
        if (fileType == String.class || fileType.isEnum()) {
            try {
                String strValue = String.valueOf(value);
                if (strValue.trim().startsWith("<>")) {
                    sql.append(value);
                    return sql.toString();
                }
            }
            catch (Exception e) {
            }

            sql.append(" = '");
            sql.append(value);
            sql.append("' ");
        }
        else {
            sql.append(" = ");
            sql.append(value);
        }
        return sql.toString();
    }

    protected String getFileTypeByList(Field filed, List<Object> values) {
        if (values == null || values.size() == 0) return "";
        StringBuffer sql = new StringBuffer();
        Class<?> fileType = filed.getType();
        sql.append(" in").append('(');
        for (Object value : values) {
            if (fileType == String.class) {
                sql.append("'").append(value).append("'").append(',');
            }
            else {
                sql.append(value).append(',');
            }
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(')');
        return sql.toString();
    }

    protected LinkedHashMap<String, String> buildOrderbyCondition(Map<String, Object> params) {
        LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
        if (params.containsKey("orderBy")) {
            String orderBy = (String)params.get("orderBy");
            String[] orderMap = orderBy.split(":");
            String orderKey = orderMap[0];
            String ordervalue = orderMap[1];
            List<Field> fileds = getUnTransientFields();
            for (int i = 0; i < fileds.size(); i++) {
                Field filed = fileds.get(i);
                String filedName = filed.getName();
                if (orderKey.equalsIgnoreCase(filedName)) {
                    orderby.put(filedName, ordervalue);
                }
            }
        }
        return orderby;
    }

    protected void appendCondition(Map<String, Object> params, StringBuffer jpql) {
        List<Field> fileds = getUnTransientFields();
        for (Map.Entry<String, Object> key : params.entrySet()) {
            for (int i = 0; i < fileds.size(); i++) {
                Field filed = fileds.get(i);
                String filedName = filed.getName();
                if (key.getKey().equalsIgnoreCase(filedName)) {
                    if (jpql.length() != 0) jpql.append(" and ");
                    jpql.append(" o.").append(filedName).append(getFileTypeByDim(filed, key.getValue()));
                }
            }
        }
    }

    public int batchDeleteByNativeSql(String localSql) {
        Query query = em.createNativeQuery(localSql);
        int deleteNum = query.executeUpdate();
        return deleteNum;

    }
    
    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

}
