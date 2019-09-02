package com.shop.online.dao.impl;

import com.shop.online.dao.UserDao;
import com.shop.online.model.User;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 用户基本操作的定义
 * @author YJH
 * @version V1.0 创建时间：2019/3/3 0003
 */
@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

    @Override
    public User findUser(String hql, User user) throws Exception {
        Query<User> q = this.getCurrentSession().createQuery(hql);
        q.setProperties(user);
        return q.uniqueResult();
    }

    @Override
    public int updateUser(String hql, User user) throws Exception {
        Query q  = this.getCurrentSession().createQuery(hql);
        q.setProperties(user);
       /* q.setParameter("id", user.getId());
        q.setParameter("realName", user.getRealName());
        q.setParameter("telePhone", user.getTelePhone());
        q.setParameter("gender", user.getGender());
        q.setParameter("birthday", user.getBirthday());*/
        return q.executeUpdate();
    }

    @Override
    public int updatePassw(String hql, User user) throws Exception{
        Query q = this.getCurrentSession().createQuery(hql);
        q.setProperties(user);
        return q.executeUpdate();
    }

    @Override
    public int updatePassword2(String hql, Map<String, String> map) {
        Query q = this.getCurrentSession().createQuery(hql);
        if(map!=null && !map.isEmpty()) {
            for( String key : map.keySet()) {
                q.setParameter(key, map.get(key));
            }
        }
        return q.executeUpdate();
    }

    @Override
    public Long countUser_admin(String hql, Map<String, Object> map) {
        Query q = this.getCurrentSession().createQuery(hql);
        if(map!=null && !map.isEmpty()) {
            for( String key : map.keySet()) {
                q.setParameter(key, map.get(key));
            }
        }
        return (Long)q.list().get(0);
    }

    @Override
    public int deleteUserBatch(String hql, String[] userId) {
        Query q = this.getCurrentSession().createQuery(hql);
        q.setParameterList("userId", userId);
        return q.executeUpdate();
    }
}
