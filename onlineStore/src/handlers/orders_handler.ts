
import express from 'express';
import { OrderClass } from '../models/orders';
import jwt from 'jsonwebtoken';

export enum status {
  active = 'active',
  shipped = 'shipped',
}

export type order = {
  id?: number;
  user_id: number;
  status: status;
};

const orderClass = new OrderClass();

export const show = async (req: express.Request, res: express.Response) => {
  try {
    jwt.verify(req.body.token, process.env.TOKEN_SECRET as string);
  } catch {
    res.status(401);
    res.json('Access denied, invalid token' );
    return;
  }

  const id = Number(req.params.id);

  let order: order | null;
  try {
    order = await orderClass.show(id);
    if (!order) {
      res.status(400);
      res.json('order not found');
    }
    res.json(order);
  } catch (err) {
    res.status(400);
    res.json(err);
  }
};

export const create = async (req: express.Request, res: express.Response) => {
  try {
    jwt.verify(req.body.token, process.env.TOKEN_SECRET as string);
  } catch (err) {
    return res.status(401).json({ error: `Access denied, invalid token ${err}`});

  }

  if (!req.body.status || !req.body.user_id) {
    return res.status(400).json({ error: 'Missing required fields' });
   
  }

  const order = {
    user_id: req.body.user_id,
    status: req.body.status,
  };

  try {
    const newOrder = await orderClass.create(order);
    res.json(newOrder);
  } catch (err) {
    res.status(400);
    res.json(err);
  }
};

export const addProductToOrder = async (req: express.Request,res: express.Response,) => {
  try {
    jwt.verify(req.body.token, process.env.TOKEN_SECRET as string);
  } catch (err) {
    return res.status(401).json({ error: `Access denied, invalid token ${err}`});

  }

  const orderId = Number(req.params.orderId);
  const productId = Number(req.params.productId);
  const quantity = Number(req.params.quantity);

  let order: order | null;

  try {
    order = await orderClass.show(orderId);
    if (!order) {
        return res.status(404).json({ error: 'Order not found' });

    }
  } catch (err) {
    res.status(400);
    res.json(err);
    return;
  }

  if (order.status !== 'active') {
    return res.status(400).json({ error: 'Order is not active' });

  }

  if (quantity <= 0) {
    return res.status(400).json({ error: 'Quantity must be greater than 0' });

  }

  try {
    const newOrder = await orderClass.addProduct(
        quantity,
        orderId,
      productId
      
    );
    res.json(newOrder);
  } catch (err) {
    res.status(400);
    res.json(err);
  }
};

const orderRoutes = (app: express.Application) => {
  app.get('/orders/:id', show);
  app.post('/orders', create);
  app.post('/orders/:orderId/products/:productId/:quantity', addProductToOrder);
};

export default orderRoutes;