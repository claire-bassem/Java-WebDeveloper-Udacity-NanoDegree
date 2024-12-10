import express, { Request, Response } from 'express'
import {  Product, ProductClass } from '../models/products'
import jwt from 'jsonwebtoken';

const store = new ProductClass();

const index = async (_req: Request, res: Response) => {
    try {
      const products = await store.index();
      res.json(products);
    } catch (error) {
      console.error("Error fetching products:", error);
      res.status(500).json({ error: "Failed to fetch products" });
    }
  };
  
  const show = async (req: Request, res: Response) => {
    try {
      const product = await store.show(req.params.id);
      if (!product) {
        return res.status(404).json({ error: "Product not found" });
      }
      res.json(product);
    } catch (error) {
      console.error(`Error fetching product with ID ${req.params.id}:`, error);
      res.status(500).json({ error: "Failed to fetch product" });
    }
  };
  

const create = async (req: Request, res: Response) => {
    try {
        const authorizationHeader = req.headers.authorization;
if (!authorizationHeader) {
    res.status(401).json({ error: 'Authorization header is missing' });
    return;
}
const token = authorizationHeader.split(' ')[1];
const secret = process.env.TOKEN_SECRET;
if (!secret) {
    res.status(500).json({ error: 'Token secret is not defined in the environment' });
    return;
}
jwt.verify(token, secret);

    } catch (err: unknown) {
        console.error('Error during products creation:', err);
        res.status(401).json({ error: 'Access denied, invalid token' });
        return;
    }
    
    

    try {
        const product: Product = {
            name: req.body.name,
            price: req.body.price,

        }

        const newProduct = await store.create(product)
        res.json(newProduct)
    } catch(err) {
        res.status(400)
        res.json(err)
    }
}


const productsRoutes = (app: express.Application) => {
  app.get('/products', index)
  app.get('/products/:id', show)
  app.post('/products', create)
}

export default productsRoutes