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
 * EvaluationField entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.EvaluationField
 * @author MyEclipse Persistence Tools
 */
public class EvaluationFieldDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(EvaluationFieldDAO.class);
	// property constants
	public static final String FIELD_CONTENT = "fieldContent";
	public static final String RESULT_TYPE = "fieldContent";
	public static final String EVALUATIONFIELD_ID = "evaluationFieldID";

	public void save(EvaluationField transientInstance) {
		log.debug("saving EvaluationField instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EvaluationField persistentInstance) {
		log.debug("deleting EvaluationField instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EvaluationField findById(java.lang.Integer id) {
		log.debug("getting EvaluationField instance with id: " + id);
		try {
			EvaluationField instance = (EvaluationField) getSession().get(
					"edu.dufe.oes.bean.EvaluationField", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EvaluationField instance) {
		log.debug("finding EvaluationField instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.EvaluationField")
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
		log.debug("finding EvaluationField instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from EvaluationField as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByFieldContent(Object fieldContent) {
		return findByProperty(FIELD_CONTENT, fieldContent);
	}

	public List findAll() {
		log.debug("finding all EvaluationField instances");
		try {
			String queryString = "from EvaluationField";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public EvaluationField merge(EvaluationField detachedInstance) {
		log.debug("merging EvaluationField instance");
		try {
			EvaluationField result = (EvaluationField) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EvaluationField instance) {
		log.debug("attaching dirty EvaluationField instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EvaluationField instance) {
		log.debug("attaching clean EvaluationField instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}