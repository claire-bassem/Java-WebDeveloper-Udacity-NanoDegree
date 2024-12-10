import { OrderClass , status} from '../orders';
import Client from '../../database';
import { Order } from '../orders';

const store = new OrderClass(); // Create an instance of OrderClass

describe("Order Model", () => {
  let createdOrder: Order;

  // Before all tests, create a sample order for testing
  beforeAll(async () => {
    createdOrder = await store.create({
      status: status.active,
      user_id: 1,
    });
  });

  // After all tests, delete the created order (optional depending on your database setup)
  afterAll(async () => {
    const conn = await Client.connect();
    const sql = 'DELETE FROM orders WHERE id = $1';
    await conn.query(sql, [createdOrder.id]);
    conn.release();
  });

  // Test if index method is defined
  it('should have an index method', () => {
    expect(store.index).toBeDefined();
  });

  // Test if show method is defined
  it('should have a show method', () => {
    expect(store.show).toBeDefined();
  });

  // Test if create method is defined
  it('should have a create method', () => {
    expect(store.create).toBeDefined();
  });

  // Test if addProduct method is defined
  it('should have an addProduct method', () => {
    expect(store.addProduct).toBeDefined();
  });

  // Test the index method to return a list of orders
  it('index method should return a list of orders', async () => {
    const result = await store.index();
    expect(result[0]).toEqual({
      id: createdOrder.id,
      status: createdOrder.status,
      user_id: createdOrder.user_id,
    });
  });
  
  // Test the show method to return the correct order by id
  it('show method should return the correct order', async () => {
    if (createdOrder.id === undefined) {
        throw new Error('Order ID is undefined');
      }
    const result = await store.show(createdOrder.id);
    expect(result).toEqual({
      id: createdOrder.id,
      status: createdOrder.status,
      user_id: createdOrder.user_id,
    });
  });



  
});
