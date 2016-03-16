package edu.dufe.oes.bean;

import java.util.List;
import java.util.Set;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for Group
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see edu.dufe.oes.bean.Group
 * @author MyEclipse Persistence Tools
 */
public class GroupDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(GroupDAO.class);
	// property constants
	public static final String GROUP_NUMBER = "groupNumber";
	public static final String GROUP_ID = "groupID";

	public void save(Group transientInstance) {
		log.debug("saving Group instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Group persistentInstance) {
		log.debug("deleting Group instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Group findById(java.lang.Integer id) {
		log.debug("getting Group instance with id: " + id);
		try {
			Group instance = (Group) getSession().get(
					"edu.dufe.oes.bean.Group", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Group instance) {
		log.debug("finding Group instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.Group")
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
		log.debug("finding Group instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Group as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByGroupNumber(Object groupNumber) {
		return findByProperty(GROUP_NUMBER, groupNumber);
	}

	public List findAll() {
		log.debug("finding all Group instances");
		try {
			String queryString = "from Group";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Group merge(Group detachedInstance) {
		log.debug("merging Group instance");
		try {
			Group result = (Group) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Group instance) {
		log.debug("attaching dirty Group instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Group instance) {
		log.debug("attaching clean Group instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}