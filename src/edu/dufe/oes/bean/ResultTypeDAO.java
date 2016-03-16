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
 * ResultType entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.ResultType
 * @author MyEclipse Persistence Tools
 */
public class ResultTypeDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ResultTypeDAO.class);
	// property constants
	public static final String RESULT_TYPE_NAME = "resultTypeName";
	public static final String RESULT_TYPE_ID = "resultTypeID";

	public void save(ResultType transientInstance) {
		log.debug("saving ResultType instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ResultType persistentInstance) {
		log.debug("deleting ResultType instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ResultType findById(java.lang.Integer id) {
		log.debug("getting ResultType instance with id: " + id);
		try {
			ResultType instance = (ResultType) getSession().get(
					"edu.dufe.oes.bean.ResultType", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ResultType instance) {
		log.debug("finding ResultType instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.ResultType")
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
		log.debug("finding ResultType instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ResultType as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByResultTypeName(Object resultTypeName) {
		return findByProperty(RESULT_TYPE_NAME, resultTypeName);
	}

	public List findAll() {
		log.debug("finding all ResultType instances");
		try {
			String queryString = "from ResultType";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ResultType merge(ResultType detachedInstance) {
		log.debug("merging ResultType instance");
		try {
			ResultType result = (ResultType) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ResultType instance) {
		log.debug("attaching dirty ResultType instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ResultType instance) {
		log.debug("attaching clean ResultType instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}