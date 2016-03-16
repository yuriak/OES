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
 * EvaluationReceiver entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.EvaluationReceiver
 * @author MyEclipse Persistence Tools
 */
public class EvaluationReceiverDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(EvaluationReceiverDAO.class);
	// property constants
	public static final String EVALUATION_GRADE = "evaluationGrade";
	public static final String RECEIVER_TYPE = "receiverType";

	public void save(EvaluationReceiver transientInstance) {
		log.debug("saving EvaluationReceiver instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(EvaluationReceiver persistentInstance) {
		log.debug("deleting EvaluationReceiver instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EvaluationReceiver findById(java.lang.Integer id) {
		log.debug("getting EvaluationReceiver instance with id: " + id);
		try {
			EvaluationReceiver instance = (EvaluationReceiver) getSession()
					.get("edu.dufe.oes.bean.EvaluationReceiver", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(EvaluationReceiver instance) {
		log.debug("finding EvaluationReceiver instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.EvaluationReceiver")
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
		log.debug("finding EvaluationReceiver instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from EvaluationReceiver as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByEvaluationGrade(Object evaluationGrade) {
		return findByProperty(EVALUATION_GRADE, evaluationGrade);
	}

	public List findByReceiverType(Object receiverType) {
		return findByProperty(RECEIVER_TYPE, receiverType);
	}

	public List findAll() {
		log.debug("finding all EvaluationReceiver instances");
		try {
			String queryString = "from EvaluationReceiver";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public EvaluationReceiver merge(EvaluationReceiver detachedInstance) {
		log.debug("merging EvaluationReceiver instance");
		try {
			EvaluationReceiver result = (EvaluationReceiver) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(EvaluationReceiver instance) {
		log.debug("attaching dirty EvaluationReceiver instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EvaluationReceiver instance) {
		log.debug("attaching clean EvaluationReceiver instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}