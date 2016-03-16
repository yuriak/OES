package edu.dufe.oes.bean;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for Leave
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see edu.dufe.oes.bean.Leave
 * @author MyEclipse Persistence Tools
 */
public class LeaveDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(LeaveDAO.class);
	// property constants
	public static final String LEAVE_REASON = "leaveReason";
	public static final String _APPROVE_STATUS = "ApproveStatus";
	public static final String LEAVE_ID="leaveID";

	public void save(Leave transientInstance) {
		log.debug("saving Leave instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Leave persistentInstance) {
		log.debug("deleting Leave instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Leave findById(java.lang.Integer id) {
		log.debug("getting Leave instance with id: " + id);
		try {
			Leave instance = (Leave) getSession().get(
					"edu.dufe.oes.bean.Leave", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Leave instance) {
		log.debug("finding Leave instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.Leave")
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
		log.debug("finding Leave instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Leave as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByLeaveReason(Object leaveReason) {
		return findByProperty(LEAVE_REASON, leaveReason);
	}

	public List findByApproveStatus(Object ApproveStatus) {
		return findByProperty(_APPROVE_STATUS, ApproveStatus);
	}

	public List findAll() {
		log.debug("finding all Leave instances");
		try {
			String queryString = "from Leave";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Leave merge(Leave detachedInstance) {
		log.debug("merging Leave instance");
		try {
			Leave result = (Leave) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Leave instance) {
		log.debug("attaching dirty Leave instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Leave instance) {
		log.debug("attaching clean Leave instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}