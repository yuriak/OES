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
 * GroupTemplate entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.GroupTemplate
 * @author MyEclipse Persistence Tools
 */
public class GroupTemplateDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(GroupTemplateDAO.class);
	// property constants
	public static final String GROUP_TEMPLATE_NAME = "groupTemplateName";
	public static final String GROUP_BACKUP_ID = "groupBackupID";
	public static final String GROUP_TEMPLATEID = "groupTemplateID";

	public void save(GroupTemplate transientInstance) {
		log.debug("saving GroupTemplate instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(GroupTemplate persistentInstance) {
		log.debug("deleting GroupTemplate instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public GroupTemplate findById(java.lang.Integer id) {
		log.debug("getting GroupTemplate instance with id: " + id);
		try {
			GroupTemplate instance = (GroupTemplate) getSession().get(
					"edu.dufe.oes.bean.GroupTemplate", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(GroupTemplate instance) {
		log.debug("finding GroupTemplate instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.GroupTemplate")
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
		log.debug("finding GroupTemplate instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from GroupTemplate as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByGroupTemplateName(Object groupTemplateName) {
		return findByProperty(GROUP_TEMPLATE_NAME, groupTemplateName);
	}

	public List findByGroupBackupID(Object groupBackupID) {
		return findByProperty(GROUP_BACKUP_ID, groupBackupID);
	}

	public List findAll() {
		log.debug("finding all GroupTemplate instances");
		try {
			String queryString = "from GroupTemplate";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public GroupTemplate merge(GroupTemplate detachedInstance) {
		log.debug("merging GroupTemplate instance");
		try {
			GroupTemplate result = (GroupTemplate) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(GroupTemplate instance) {
		log.debug("attaching dirty GroupTemplate instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(GroupTemplate instance) {
		log.debug("attaching clean GroupTemplate instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}