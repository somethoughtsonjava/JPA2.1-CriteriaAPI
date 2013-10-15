package blog.thoughts.on.java.jpa21.criteria.business;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import blog.thoughts.on.java.jpa21.criteria.model.Order;

@Stateless
@LocalBean
public class OrderManagement
{
	@PersistenceContext
	private EntityManager em;
	
	public Order createOrder(Order order) {
		this.em.persist(order);
		return order;
	}
	
	public List<Order> getOrders() {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery<Order> query = cb.createQuery(Order.class);
		Root e = query.from(Order.class);
		return this.em.createQuery(query).getResultList();
	}
	
	public void updateOrder(Double oldAmount, Double newAmount) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		
		// create update
		CriteriaUpdate<Order> update = cb.
							createCriteriaUpdate(Order.class);
		
		// set the root class
		Root e = update.from(Order.class);
		
		// set update and where clause
		update.set("amount", newAmount);
		update.where(cb.greaterThanOrEqualTo(e.get("amount"), 
												oldAmount));
		
		// perform update
		this.em.createQuery(update).executeUpdate();
	}
	
	public void deleteOrder(Double amount) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		
		// create delete
		CriteriaDelete<Order> delete = cb.
							createCriteriaDelete(Order.class);
		
		// set the root class
		Root e = delete.from(Order.class);
		
		// set where clause
		delete.where(cb.lessThanOrEqualTo(e.get("amount"), amount));
		
		// perform update
		this.em.createQuery(delete).executeUpdate();
	}
}