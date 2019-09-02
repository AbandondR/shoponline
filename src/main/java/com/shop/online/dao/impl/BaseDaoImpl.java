package com.shop.online.dao.impl;

import com.shop.online.dao.BaseDao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/1 0001
 */
@Repository
@Transactional
public class BaseDaoImpl<T> implements BaseDao<T> {
    @Resource
    private SessionFactory sessionFactory;
    private Class<T> clz;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }

    private Class<T> getClz() {
        if (clz == null) {
            //获取泛型的Class对象
            clz = ((Class<T>)
                    (((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
        }
        return clz;
    }

    @Override
    public Serializable save(T o) {

        return this.getCurrentSession().save(o);
    }

    @Override
    public void saveOrUpdate(T o) {
        this.getCurrentSession().saveOrUpdate(o);
    }

    @Override
    public void update(T o) {
        this.getCurrentSession().update(o);
    }

    @Override
    public void delete(T o) {
        this.getCurrentSession().delete(o);
    }

    @Override
    public void delete(Serializable id) {
        T o = this.get(id);
        this.getCurrentSession().delete(o);
    }

    @Override
    public T get(Serializable id) {
        return (T) this.getCurrentSession().get(getClz(), id);
    }

    @Override
    public List<T> find(String hql) {
        Query q = this.getCurrentSession().createQuery(hql);
        return q.list();
    }

    @Override
    public List<T> find(String hql, Map<String, Object> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return q.list();
    }

    @Override
    public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
    }

    @Override
    public List<T> find(String hql, int page, int rows) {
        Query q = this.getCurrentSession().createQuery(hql);
        return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
    }

    @Override
    public Integer count(String hql) {
        Query q = sessionFactory.getCurrentSession().createQuery(hql);
        List cc = q.list();
        return cc.size();
    }

    @Override
    public Integer count(String hql, Integer id) {
        Query q = sessionFactory.getCurrentSession().createQuery(hql);
        q.setParameter(0, id);
        List cc = q.list();
        return cc.size();
    }

    @Override
    public Integer count(String hql, Map<String, Object> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return (Integer) q.list().size();
    }

    @Override
    public int executeHql(String hql) {
        Query q = this.getCurrentSession().createQuery(hql);
        return q.executeUpdate();
    }

    @Override
    public int executeHql(String hql, Map<String, Object> params) {
        Query q = this.getCurrentSession().createQuery(hql);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                q.setParameter(key, params.get(key));
            }
        }
        return q.executeUpdate();
    }

    @Override
    public Integer findByUid(Integer uid) {
        // TODO Auto-generated method stub
        return null;

    }

    @Override
    public Integer findTicketByCid(Integer cid) {
        // TODO Auto-generated method stub
        return null;
    }
}
