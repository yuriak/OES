package edu.dufe.oes.bean;

import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * MultipleOptionResult entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.MultipleOptionResult
 * @author MyEclipse Persistence Tools
 */
public class MultipleOptionResultDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(MultipleOptionResultDAO.class);
	// property constants
	public static final String OPTION_VALUE = "optionValue";

	public void save(MultipleOptionResult transientInstance) {
		log.debug("saving MultipleOptionResult instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(MultipleOptionResult persistentInstance) {
		log.debug("deleting MultipleOptionResult instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MultipleOptionResult findById(java.lang.Integer id) {
		log.debug("getting MultipleOptionResult instance with id: " + id);
		try {
			MultipleOptionResult instance = (MultipleOptionResult) getSession()
					.get("edu.dufe.oes.bean.MultipleOptionResult", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(MultipleOptionResult instance) {
		log.debug("finding MultipleOptionResult instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.MultipleOptionResult")
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
		log.debug("finding MultipleOptionResult instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from MultipleOptionResult as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByOptionValue(Object optionValue) {
		return findByProperty(OPTION_VALUE, optionValue);
	}

	public List findAll() {
		log.debug("finding all MultipleOptionResult instances");
		try {
			String queryString = "from MultipleOptionResult";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public MultipleOptionResult merge(MultipleOptionResult detachedInstance) {
		log.debug("merging MultipleOptionResult instance");
		try {
			MultipleOptionResult result = (MultipleOptionResult) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(MultipleOptionResult instance) {
		log.debug("attaching dirty MultipleOptionResult instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MultipleOptionResult instance) {
		log.debug("attaching clean MultipleOptionResult instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}