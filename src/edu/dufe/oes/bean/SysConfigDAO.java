package edu.dufe.oes.bean;

import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * SysConfig entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.SysConfig
 * @author MyEclipse Persistence Tools
 */
public class SysConfigDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(SysConfigDAO.class);
	// property constants
	public static final String CONFIG_KEY = "configKey";
	public static final String CONFIG_VALUE = "configValue";

	public void save(SysConfig transientInstance) {
		log.debug("saving SysConfig instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SysConfig persistentInstance) {
		log.debug("deleting SysConfig instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SysConfig findById(java.lang.Integer id) {
		log.debug("getting SysConfig instance with id: " + id);
		try {
			SysConfig instance = (SysConfig) getSession().get(
					"edu.dufe.oes.bean.SysConfig", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SysConfig instance) {
		log.debug("finding SysConfig instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.SysConfig")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding SysConfig instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from SysConfig as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByConfigKey(Object configKey) {
		return findByProperty(CONFIG_KEY, configKey);
	}

	public List findByConfigValue(Object configValue) {
		return findByProperty(CONFIG_VALUE, configValue);
	}

	public List findAll() {
		log.debug("finding all SysConfig instances");
		try {
			String queryString = "from SysConfig";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SysConfig merge(SysConfig detachedInstance) {
		log.debug("merging SysConfig instance");
		try {
			SysConfig result = (SysConfig) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SysConfig instance) {
		log.debug("attaching dirty SysConfig instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SysConfig instance) {
		log.debug("attaching clean SysConfig instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}