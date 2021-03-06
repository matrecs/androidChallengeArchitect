package com.whynot.checkOtrade.web.dao;

import com.whynot.checkOtrade.web.domain.Account;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Repository
@Transactional(readOnly = true)
public class AccountRepository {

	

	@Autowired
	SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public Account create(Account account) {
		Session session = sessionFactory.openSession();

		Long id = (Long) session.save(account);

		if (session != null) {
			session.flush();
			session.close();
		}

		return findByID(id);
	}

	public Account findByID(long id) {
		Session session = sessionFactory.openSession();

		Account account = (Account) session.load(Account.class, id);
		try {
			Hibernate.initialize(account);
		} catch (ObjectNotFoundException e) {
			System.out.println("Account object with id " + id + " not found");
			account = null;
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}

		return account;
	}

	public List<Account> findAll() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Account> accounts = new ArrayList<Account>(session.createQuery("from Account").list());
		session.getTransaction().commit();
		if (session != null) {
			session.flush();
			session.close();
		}

		return accounts;
	}

	public Account findByEmail(String email) {
		Session session = sessionFactory.openSession();
		Criteria cr = session.createCriteria(Account.class);
		cr.add(Restrictions.eq("email", email));	
		Account account = (Account) cr.uniqueResult();
		try {
			Hibernate.initialize(account);
		} catch (ObjectNotFoundException e) {
			System.out.println("Account object with id " + email + " not found");
			account = null;
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}

		return account;
	}

	public boolean delete(Account account) {
		Session session = sessionFactory.openSession();

		session.delete(account);

		Account c = findByID(account.getId());
		if (session != null) {
			session.flush();
			session.close();
		}
		return c == null;
	}

	public Account update(Account account) {
		Session session = sessionFactory.openSession();

		session.update(account);

		Account a = findByID(account.getId());
		if (session != null) {
			session.flush();
			session.close();
		}
		return a;
	}
}
