package edu.dufe.oes.bean;

import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * OptionTitle entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.OptionTitle
 * @author MyEclipse Persistence Tools
 */
public class OptionTitleDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(OptionTitleDAO.class);
	// property constants
	public static final String OPTION_KEY = "optionKey";
	public static final String OPTION_TITLE_CONTENT = "optionTitleContent";

	public void save(OptionTitle transientInstance) {
		log.debug("saving OptionTitle instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(OptionTitle persistentInstance) {
		log.debug("deleting OptionTitle instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OptionTitle findById(java.lang.Integer id) {
		log.debug("getting OptionTitle instance with id: " + id);
		try {
			OptionTitle instance = (OptionTitle) getSession().get(
					"edu.dufe.oes.bean.OptionTitle", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(OptionTitle instance) {
		log.debug("finding OptionTitle instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.OptionTitle")
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
		log.debug("finding OptionTitle instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from OptionTitle as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByOptionKey(Object optionKey) {
		return findByProperty(OPTION_KEY, optionKey);
	}

	public List findByOptionTitleContent(Object optionTitleContent) {
		return findByProperty(OPTION_TITLE_CONTENT, optionTitleContent);
	}

	public List findAll() {
		log.debug("finding all OptionTitle instances");
		try {
			String queryString = "from OptionTitle";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OptionTitle merge(OptionTitle detachedInstance) {
		log.debug("merging OptionTitle instance");
		try {
			OptionTitle result = (OptionTitle) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OptionTitle instance) {
		log.debug("attaching dirty OptionTitle instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OptionTitle instance) {
		log.debug("attaching clean OptionTitle instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}