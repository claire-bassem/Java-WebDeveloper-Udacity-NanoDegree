import supertest from 'supertest';
import jwt from 'jsonwebtoken';
import app from '../../server';
import { OrderClass } from '../orders';
import client from '../../database';
const request = supertest(app);
const order_class = new OrderClass();

const cleanUpDatabase = async () => {
  await client.query('ALTER SEQUENCE orders_id_seq RESTART WITH 1');
};



enum status {
  active = 'active',
  shipped = 'shipped',
}

export type order = {
  id?: number;
  userid: number;
  status: status;
};

const mockProduct = {
  name: 'book',
  price: 143,
};

// A mock user object for creating and testing
const mockUser = {
  firstname: 'koko',
  lastname: 'koko',
  password: 'password123',
};

describe('Order Route', () => {
  let token: string;
  beforeAll(() => {
    token = jwt.sign({ user: mockUser }, process.env.TOKEN_SECRET as string);
    cleanUpDatabase();

  });

  describe('POST /orders', () => {
    it('should require a token', async () => {
      const res = await request.post('/orders').send({
        userid: 1,
        status: 'active',
      });
      expect(res.status).toBe(401);
    });

  });
    it('should create a new order', async () => {
      spyOn(order_class, 'create').and.returnValue(
        Promise.resolve({ id: 1, userid: 1, status: 'active' } as order),
      );
      const res = await request.post('/orders').send({
        userid: 1,
        status: 'active',
        token,
      });
      expect(res.status).toBe(200);
      expect(res.body).toEqual({
        id: 1,
        userid: '1',
        status: 'active',
      });
    });

    it('should create a new order', async () => {
      spyOn(order_class, 'create').and.returnValue(
        Promise.resolve({ id: 2, userid: 2, status: 'complete' }),
      );
      const res = await request.post('/orders').send({
        userid: 2,
        status: 'complete',
        token,
      });
      expect(res.status).toBe(200);
      expect(res.body).toEqual({
        id: 2,
        userid: '2',
        status: 'complete',
      });
    });


  describe('GET /orders/:id', () => {
    it('should require a token', async () => {
      const res = await request.get('/orders/1');
      expect(res.status).toBe(401);
      expect(res.body).toEqual(`error: invalid token`);
    });
  });

  describe('POST /orders/:orderId/products/:productId/:quantity', () => {
    it('should require a token', async () => {
      const res = await request.post('/orders/1/products/1/10');
      expect(res.status).toBe(401);
    });


  });
  it('should create a new product with valid token', async () => {
    const response = await request
      .post('/products')
      .send({ ...mockProduct, token });

    expect(response.status).toBe(200);
    expect(response.body.name).toBe(mockProduct.name);
  });

    it('should add a product to an order', async () => {
      spyOn(order_class, 'addProduct').and.returnValue(
        Promise.resolve({ orderId: 1, productId: 1, quantity: 10 }),
      );
      spyOn(order_class, 'show').and.returnValue(
        Promise.resolve({ id: 1, userid: 1, status: 'active' } as order),
      );

      const res = await request.post('/orders/1/products/1/10').send({ token });
      expect(res.status).toBe(200);
      expect(res.body).toEqual({ orderid: 1, productid: 1, quantity: 10 });
    });


    it('should return 404 if order is not found', async () => {
      spyOn(order_class, 'show').and.returnValue(
        Promise.resolve({ id: 20, userid: 1, status: 'active' }),
      );
      const res = await request.post('/orders/20/products/1/10').send({ token });
      expect(res.status).toBe(404);
    });

   
  });

