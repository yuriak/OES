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
 * CourseElective entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see edu.dufe.oes.bean.CourseElective
 * @author MyEclipse Persistence Tools
 */
public class CourseElectiveDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(CourseElectiveDAO.class);
	// property constants
	public static final String ELECTIVE_STATUS = "electiveStatus";
	public static final String COURSE_GRADE = "courseGrade";
	public static final String COURSE_GRADE_STATUS = "courseGradeStatus";

	public void save(CourseElective transientInstance) {
		log.debug("saving CourseElective instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CourseElective persistentInstance) {
		log.debug("deleting CourseElective instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CourseElective findById(java.lang.Integer id) {
		log.debug("getting CourseElective instance with id: " + id);
		try {
			CourseElective instance = (CourseElective) getSession().get(
					"edu.dufe.oes.bean.CourseElective", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CourseElective instance) {
		log.debug("finding CourseElective instance by example");
		try {
			List results = getSession()
					.createCriteria("edu.dufe.oes.bean.CourseElective")
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
		log.debug("finding CourseElective instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from CourseElective as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByElectiveStatus(Object electiveStatus) {
		return findByProperty(ELECTIVE_STATUS, electiveStatus);
	}

	public List findByCourseGrade(Object courseGrade) {
		return findByProperty(COURSE_GRADE, courseGrade);
	}

	public List findByCourseGradeStatus(Object courseGradeStatus) {
		return findByProperty(COURSE_GRADE_STATUS, courseGradeStatus);
	}

	public List findAll() {
		log.debug("finding all CourseElective instances");
		try {
			String queryString = "from CourseElective";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CourseElective merge(CourseElective detachedInstance) {
		log.debug("merging CourseElective instance");
		try {
			CourseElective result = (CourseElective) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CourseElective instance) {
		log.debug("attaching dirty CourseElective instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CourseElective instance) {
		log.debug("attaching clean CourseElective instance");
		try {
			getSession().buildLockRequest(LockOptions.NONE).lock(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}