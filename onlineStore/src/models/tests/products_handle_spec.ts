import supertest from 'supertest';
import jwt from 'jsonwebtoken';
import app from '../../server';
import { ProductClass } from '../products';
const request = supertest(app);

// Mock the ProductClass and its methods
jest.mock('../models/products', () => {
  return {
    ProductClass: jest.fn().mockImplementation(() => ({
      index: jest.fn(),
      show: jest.fn(),
      create: jest.fn(),
    })),
  };
});

describe('Product Routes', () => {
  let token: string;
  const mockProduct = {
    id: 1,
    name: 'Product 1',
    price: 100,
  };

  beforeAll(() => {
    token = jwt.sign({ user: { id: 1, name: 'John Doe' } }, process.env.TOKEN_SECRET as string);
  });

  describe('GET /products', () => {
    it('should return a list of products', async () => {
      const mockProducts = [mockProduct];
      (ProductClass.prototype.index as jest.Mock).mockResolvedValue(mockProducts);

      const res = await request.get('/products');

      expect(res.status).toBe(200);
      expect(res.body).toEqual(mockProducts);
      expect(ProductClass.prototype.index).toHaveBeenCalled();
    });
  });

  describe('GET /products/:id', () => {
    it('should return a product by ID', async () => {
      (ProductClass.prototype.show as jest.Mock).mockResolvedValue(mockProduct);
  
      const res = await request.get(`/products/${mockProduct.id}`);
  
      expect(res.status).toBe(200);
      expect(res.body).toEqual(mockProduct);
      expect(ProductClass.prototype.show).toHaveBeenCalledWith(mockProduct.id.toString());
    });
  });
  

  describe('POST /products', () => {
    it('should return 401 if authorization header is missing', async () => {
      const res = await request.post('/products').send({
        name: 'Product 2',
        price: 200,
      });

      expect(res.status).toBe(401);
      expect(res.body.error).toBe('Authorization header is missing');
    });

    it('should return 401 if token is invalid', async () => {
      const invalidToken = 'invalidToken';
      const res = await request
        .post('/products')
        .set('Authorization', `Bearer ${invalidToken}`)
        .send({
          name: 'Product 2',
          price: 200,
        });

      expect(res.status).toBe(401);
      expect(res.body.error).toBe('Access denied, invalid token');
    });

    it('should create a new product and return it', async () => {
      (ProductClass.prototype.create as jest.Mock).mockResolvedValue(mockProduct);

      const res = await request
        .post('/products')
        .set('Authorization', `Bearer ${token}`)
        .send({
          name: 'Product 2',
          price: 200,
        });

      expect(res.status).toBe(200);
      expect(res.body).toEqual(mockProduct);
      expect(ProductClass.prototype.create).toHaveBeenCalledWith({
        name: 'Product 2',
        price: 200,
      });
    });

    it('should return 500 if token secret is missing', async () => {
      process.env.TOKEN_SECRET = '';  // Simulate missing secret

      const res = await request
        .post('/products')
        .set('Authorization', `Bearer ${token}`)
        .send({
          name: 'Product 2',
          price: 200,
        });

      expect(res.status).toBe(500);
      expect(res.body.error).toBe('Token secret is not defined in the environment');
    });
  });
});
