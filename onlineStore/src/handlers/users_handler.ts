import express, { Request, Response, NextFunction } from 'express';
import { User, UserClass } from '../models/users';
import jwt from 'jsonwebtoken';
import bcrypt from 'bcrypt';

const store = new UserClass();

// Middleware for token validation
const verifyToken = (req: Request, res: Response, next: NextFunction) => {
  const token = req.headers.authorization?.split(' ')[1]; // Extract the token from the `Authorization` header

  if (!token) {
    return res.status(401).json({ error: 'Access denied, token missing' });
  }

  try {
    const decoded = jwt.verify(token, process.env.TOKEN_SECRET as string);
    (req as any).user = decoded; // Attach decoded token to the request object for further use
    /*In TypeScript, req is typically defined as an object of the Request type, 
    which comes from the express module. 
    By default, this type does not include any custom properties like user. 
    Therefore, when adding properties, TypeScript doesn't recognize them as part of the Request object.
    This is why I am defining  (req as any) in this situation.
    */
    next(); // Proceed to the next middleware or route handler
  } catch (err) {
    res.status(403).json({ error: 'Invalid token' });
  }
};

const index = async (_req: Request, res: Response) => {
  try {
    const users = await store.index();
    res.json(users);
  } catch (err:unknown) {
    const error = err as Error;
    console.error('Error during users fetching:', err);
    res.status(500).json({ error: `Failed to fetch users${error.message || err}` });
  }
};

const show = async (req: Request, res: Response) => {
  try {
    const user = await store.show(req.params.id);
    if (!user) {
      return res.status(404).json({ error: 'User not found' });
    }
    res.json(user);
  } catch (err:unknown) {
    const error = err as Error;
    console.error('Error during user fetching:', err);
    res.status(500).json({ error: `Failed to fetch user: ${error.message || err}` });
  }
};

const create = async (req: Request, res: Response) => {
  const { firstname, lastname, password } = req.body;

  // Validate input
  if (!firstname || !lastname || !password) {
    return res.status(400).json({ error: 'Missing required fields' });
  }

  try {
    // Hash the password before storing
    const saltRounds = 10;
    const password_digest = await bcrypt.hash(password, saltRounds);

    const user: User = {
      firstname,
      lastname,
      password_digest,
    };

    const newUser = await store.create(user);

    // Generate a token
    const token = jwt.sign({ user: newUser }, process.env.TOKEN_SECRET as string);

    // Return the token
    res.status(201).json({ token });
  } catch (err:unknown) {
    const error = err as Error;
    console.error('Error during user creation:', err);
    res.status(400).json({ error: `Failed to create user: ${error.message || err}` });
  }
};


const userRoutes = (app: express.Application) => {
  app.get('/users', verifyToken, index); 
  app.get('/users/:id', verifyToken, show); 
  app.post('/users', create); 
};

export default userRoutes;
