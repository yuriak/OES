package edu.dufe.oes.bean;

import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * ValueResult entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.ValueResult
 * @author MyEclipse Persistence Tools
 */
public class ValueResultDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ValueResultDAO.class);
	// property constants
	public static final String RESULT_VALUE = "resultValue";

	public void save(ValueResult transientInstance) {
		log.debug("saving ValueResult instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ValueResult persistentInstance) {
		log.debug("deleting ValueResult instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ValueResult findById(java.lang.Integer id) {
		log.debug("getting ValueResult instance with id: " + id);
		try {
			ValueResult instance = (ValueResult) getSession().get(
					"edu.dufe.oes.bean.ValueResult", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ValueResult instance) {
		log.debug("finding ValueResult instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.ValueResult")
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
		log.debug("finding ValueResult instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ValueResult as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByResultValue(Object resultValue) {
		return findByProperty(RESULT_VALUE, resultValue);
	}

	public List findAll() {
		log.debug("finding all ValueResult instances");
		try {
			String queryString = "from ValueResult";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ValueResult merge(ValueResult detachedInstance) {
		log.debug("merging ValueResult instance");
		try {
			ValueResult result = (ValueResult) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ValueResult instance) {
		log.debug("attaching dirty ValueResult instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ValueResult instance) {
		log.debug("attaching clean ValueResult instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}