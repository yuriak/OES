package edu.dufe.oes.bean;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * EvaluationTemplate entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.EvaluationTemplate
 * @author MyEclipse Persistence Tools
 */
public class EvaluationTemplateDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(EvaluationTemplateDAO.class);
	// property constants
	public static final String EVALUATION_TEMPLATE_NAME = "evaluationTemplateName";

	public void save(EvaluationTemplate transientInstance) {
		log.debug("saving EvaluationTemplate instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EvaluationTemplate persistentInstance) {
		log.debug("deleting EvaluationTemplate instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EvaluationTemplate findById(java.lang.Integer id) {
		log.debug("getting EvaluationTemplate instance with id: " + id);
		try {
			EvaluationTemplate instance = (EvaluationTemplate) getSession()
					.get("edu.dufe.oes.bean.EvaluationTemplate", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EvaluationTemplate instance) {
		log.debug("finding EvaluationTemplate instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.EvaluationTemplate")
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
		log.debug("finding EvaluationTemplate instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from EvaluationTemplate as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByEvaluationTemplateName(Object evaluationTemplateName) {
		return findByProperty(EVALUATION_TEMPLATE_NAME, evaluationTemplateName);
	}

	public List findAll() {
		log.debug("finding all EvaluationTemplate instances");
		try {
			String queryString = "from EvaluationTemplate";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public EvaluationTemplate merge(EvaluationTemplate detachedInstance) {
		log.debug("merging EvaluationTemplate instance");
		try {
			EvaluationTemplate result = (EvaluationTemplate) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EvaluationTemplate instance) {
		log.debug("attaching dirty EvaluationTemplate instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EvaluationTemplate instance) {
		log.debug("attaching clean EvaluationTemplate instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}