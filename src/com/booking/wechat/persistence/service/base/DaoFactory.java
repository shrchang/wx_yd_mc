package com.booking.wechat.persistence.service.base;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;

/**
 * Hibernate的Dao获取工厂
 * 
 * @author Luoxx
 */
@SuppressWarnings("unchecked")
public class DaoFactory {
	private static Logger log = Logger.getLogger(DaoFactory.class);

	/**
	 * 获取DAO
	 * 
	 * @param dao
	 *            要获取DAO的class
	 * @return
	 * @throws Exception
	 */
	public static <T> T getDao(Class<T> daoClass) {
		try {
			String repositoryValue = getRepositoryValue(daoClass);
			if (repositoryValue == null) {
				return null;
			}

			ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			T dao = (T) context.getBean(repositoryValue);
			return dao;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HibernateException("获取" + daoClass.getCanonicalName()
					+ "失败" + e.getMessage(), e);
		}
	}

	private static <T> String getRepositoryValue(Class<T> daoClass)
			throws EasyHibernateException {
		String repositoryValue = "";
		try {
			Service repository = (Service) daoClass.getAnnotation(Service.class);
			if (repository == null) {
				return null;
			}
			repositoryValue = repository.value();
			if (StringUtils.isEmpty(repositoryValue)) {
				// 若Service标注的值为空，则取类的简单名称，并将首字母转为小写。
				// 如：DaoFactory -->daoFactory,因Spring 的Service Bean的默认名字是这样
				String simpleName = daoClass.getSimpleName();
				repositoryValue = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
			}
		} catch (Exception e) {
			log.error("Dao必需加上Service标注.", e);
			throw new EasyHibernateException("Dao必需加上Service标注.");
		}
		return repositoryValue;
	}

}
