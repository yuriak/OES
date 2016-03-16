package edu.dufe.oes.bean;

import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * SingleOptionResult entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.SingleOptionResult
 * @author MyEclipse Persistence Tools
 */
public class SingleOptionResultDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(SingleOptionResultDAO.class);
	// property constants
	public static final String OPTION_VALUE = "optionValue";

	public void save(SingleOptionResult transientInstance) {
		log.debug("saving SingleOptionResult instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SingleOptionResult persistentInstance) {
		log.debug("deleting SingleOptionResult instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SingleOptionResult findById(java.lang.Integer id) {
		log.debug("getting SingleOptionResult instance with id: " + id);
		try {
			SingleOptionResult instance = (SingleOptionResult) getSession()
					.get("edu.dufe.oes.bean.SingleOptionResult", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SingleOptionResult instance) {
		log.debug("finding SingleOptionResult instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.SingleOptionResult")
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
		log.debug("finding SingleOptionResult instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SingleOptionResult as model where model."
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
		log.debug("finding all SingleOptionResult instances");
		try {
			String queryString = "from SingleOptionResult";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SingleOptionResult merge(SingleOptionResult detachedInstance) {
		log.debug("merging SingleOptionResult instance");
		try {
			SingleOptionResult result = (SingleOptionResult) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SingleOptionResult instance) {
		log.debug("attaching dirty SingleOptionResult instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SingleOptionResult instance) {
		log.debug("attaching clean SingleOptionResult instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}