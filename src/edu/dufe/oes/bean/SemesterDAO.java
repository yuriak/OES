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
 * Semester entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.Semester
 * @author MyEclipse Persistence Tools
 */
public class SemesterDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(SemesterDAO.class);
	// property constants
	public static final String SEMESTER_NAME = "semesterName";

	public void save(Semester transientInstance) {
		log.debug("saving Semester instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Semester persistentInstance) {
		log.debug("deleting Semester instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Semester findById(java.lang.Integer id) {
		log.debug("getting Semester instance with id: " + id);
		try {
			Semester instance = (Semester) getSession().get(
					"edu.dufe.oes.bean.Semester", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Semester instance) {
		log.debug("finding Semester instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.Semester")
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
		log.debug("finding Semester instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Semester as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySemesterName(Object semesterName) {
		return findByProperty(SEMESTER_NAME, semesterName);
	}

	public List findAll() {
		log.debug("finding all Semester instances");
		try {
			String queryString = "from Semester";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Semester merge(Semester detachedInstance) {
		log.debug("merging Semester instance");
		try {
			Semester result = (Semester) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Semester instance) {
		log.debug("attaching dirty Semester instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Semester instance) {
		log.debug("attaching clean Semester instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}