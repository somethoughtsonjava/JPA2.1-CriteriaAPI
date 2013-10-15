package blog.thoughts.on.java.jpa21.criteria.business;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import blog.thoughts.on.java.jpa21.criteria.model.Order;

@RunWith(Arquillian.class)
public class OrderManagementTest
{
   @Inject
   private OrderManagement ordermanagement;

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class, "test.jar")
            .addClasses(OrderManagement.class, Order.class)
            .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }
   
   @Before
   public void clearOrders() {
	   this.ordermanagement.deleteOrder(Double.MIN_VALUE);
   }
   
   @Test
   public void testUpdate() {
	   Order order = new Order();
	   order.setOrderNumber("order1");
	   order.setAmount(new Double(100));  
	   this.ordermanagement.createOrder(order);
	   
	   order = new Order();
	   order.setOrderNumber("order2");
	   order.setAmount(new Double(200));  
	   this.ordermanagement.createOrder(order);

	   this.ordermanagement.updateOrder(new Double(200), new Double(500));
	   
	   List<Order> orders = this.ordermanagement.getOrders();
	   Assert.assertEquals(2, orders.size());
	   checkOrder(orders, "order1", new Double(100));
	   checkOrder(orders, "order2", new Double(500));
   }
   
   @Test
   public void testDelete() {
	   Order order = new Order();
	   order.setOrderNumber("order1");
	   order.setAmount(new Double(100));  
	   this.ordermanagement.createOrder(order);
	   
	   order = new Order();
	   order.setOrderNumber("order2");
	   order.setAmount(new Double(200));  
	   this.ordermanagement.createOrder(order);

	   this.ordermanagement.deleteOrder(new Double(100));
	   
	   List<Order> orders = this.ordermanagement.getOrders();
	   Assert.assertEquals(1, orders.size());
	   checkOrder(orders, "order2", new Double(200));
   }
	
   private void checkOrder(List<Order> orders, String orderNumber, Double amount) {
	   for (Order order : orders) {
		   if(order.getOrderNumber().equals(orderNumber)) {
			   if (order.getAmount().equals(amount)) {
				   return;
			   }
			   Assert.fail();
		   }
	   }
	   Assert.fail();;
   }
}
