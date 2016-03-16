package edu.dufe.oes.bean;

import java.util.List;
import java.util.Set;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * EvaluationResult entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.EvaluationResult
 * @author MyEclipse Persistence Tools
 */
public class EvaluationResultDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(EvaluationResultDAO.class);

	// property constants

	public void save(EvaluationResult transientInstance) {
		log.debug("saving EvaluationResult instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EvaluationResult persistentInstance) {
		log.debug("deleting EvaluationResult instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EvaluationResult findById(java.lang.Integer id) {
		log.debug("getting EvaluationResult instance with id: " + id);
		try {
			EvaluationResult instance = (EvaluationResult) getSession().get(
					"edu.dufe.oes.bean.EvaluationResult", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EvaluationResult instance) {
		log.debug("finding EvaluationResult instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.EvaluationResult")
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
		log.debug("finding EvaluationResult instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from EvaluationResult as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all EvaluationResult instances");
		try {
			String queryString = "from EvaluationResult";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public EvaluationResult merge(EvaluationResult detachedInstance) {
		log.debug("merging EvaluationResult instance");
		try {
			EvaluationResult result = (EvaluationResult) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EvaluationResult instance) {
		log.debug("attaching dirty EvaluationResult instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EvaluationResult instance) {
		log.debug("attaching clean EvaluationResult instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}