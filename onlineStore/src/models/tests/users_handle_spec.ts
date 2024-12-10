import supertest from 'supertest';
import jwt from 'jsonwebtoken';
import bcrypt from 'bcrypt';
import app from '../../server'; // Adjust based on where your express app is initialized
import { UserClass } from '../users';

const request = supertest(app);

// Mocking UserClass and its methods
jest.mock('../models/users', () => {
  return {
    UserClass: jest.fn().mockImplementation(() => ({
      index: jest.fn(),
      show: jest.fn(),
      create: jest.fn(),
    })),
  };
});

// Mocking jwt verification
jest.mock('jsonwebtoken', () => ({
  verify: jest.fn(),
  sign: jest.fn(),
}));

describe('User Routes', () => {
  let token: string;
  const mockUser = {
    id: 1,
    firstname: 'John',
    lastname: 'Doe',
    password_digest: 'hashedpassword',
  };

  beforeAll(() => {
    token = jwt.sign({ user: { id: 1, name: 'John Doe' } }, process.env.TOKEN_SECRET as string);
  });

  describe('GET /users', () => {
    it('should return a list of users if token is valid', async () => {
      const mockUsers = [mockUser];
      (UserClass.prototype.index as jest.Mock).mockResolvedValue(mockUsers);

      // Mock JWT verification to succeed
      (jwt.verify as jest.Mock).mockImplementationOnce(() => ({ user: mockUser }));

      const res = await request.get('/users').set('Authorization', `Bearer ${token}`);

      expect(res.status).toBe(200);
      expect(res.body).toEqual(mockUsers);
      expect(UserClass.prototype.index).toHaveBeenCalled();
    });

    it('should return 401 if no token is provided', async () => {
      const res = await request.get('/users');

      expect(res.status).toBe(401);
      expect(res.body.error).toBe('Access denied, token missing');
    });

    it('should return 403 if the token is invalid', async () => {
      // Simulating invalid token
      (jwt.verify as jest.Mock).mockImplementationOnce(() => { throw new Error('Invalid token'); });

      const res = await request.get('/users').set('Authorization', `Bearer ${token}`);

      expect(res.status).toBe(403);
      expect(res.body.error).toBe('Invalid token');
    });
  });

  // describe('GET /users/:id', () => {
  //   it('should return a user by ID if token is valid', async () => {
  //     (UserClass.prototype.show as jest.Mock).mockResolvedValue(mockUser);

  //     // Mock JWT verification to succeed
  //     (jwt.verify as jest.Mock).mockImplementationOnce(() => ({ user: mockUser }));

  //     const res = await request.get(`/users/${mockUser.id}`).set('Authorization', `Bearer ${token}`);

  //     expect(res.status).toBe(200);
  //     expect(res.body).toEqual(mockUser);
  //     expect(UserClass.prototype.show).toHaveBeenCalledWith(mockUser.id);
  //   });

  //   it('should return 404 if user is not found', async () => {
  //     (UserClass.prototype.show as jest.Mock).mockResolvedValue(null);

  //     // Mock JWT verification to succeed
  //     (jwt.verify as jest.Mock).mockImplementationOnce(() => ({ user: mockUser }));

  //     const res = await request.get(`/users/${mockUser.id}`).set('Authorization', `Bearer ${token}`);

  //     expect(res.status).toBe(404);
  //     expect(res.body.error).toBe('User not found');
  //   });

  //   it('should return 403 if the token is invalid', async () => {
  //     (jwt.verify as jest.Mock).mockImplementationOnce(() => { throw new Error('Invalid token'); });

  //     const res = await request.get(`/users/${mockUser.id}`).set('Authorization', `Bearer ${token}`);

  //     expect(res.status).toBe(403);
  //     expect(res.body.error).toBe('Invalid token');
  //   });
  // });


  describe('GET /users/:id', () => {
    it('should return a user by ID if token is valid', async () => {
      (UserClass.prototype.show as jest.Mock).mockResolvedValue(mockUser);
  
      // Mock JWT verification to succeed
      (jwt.verify as jest.Mock).mockImplementationOnce(() => ({ user: mockUser }));
  
      const res = await request.get(`/users/${mockUser.id}`).set('Authorization', `Bearer ${token}`);
  
      expect(res.status).toBe(200);
      expect(res.body).toEqual(mockUser);
      expect(UserClass.prototype.show).toHaveBeenCalledWith(mockUser.id.toString()); // Convert to string
    });
  });
  
  describe('POST /users', () => {
    it('should return 400 if required fields are missing', async () => {
      const res = await request.post('/users').send({ firstname: 'John' });

      expect(res.status).toBe(400);
      expect(res.body.error).toBe('Missing required fields');
    });

    it('should create a new user and return a token', async () => {
      const newUser = { firstname: 'Jane', lastname: 'Doe', password: 'password123' };
      const hashedPassword = 'hashedpassword123';
      const token = 'mocked-jwt-token';

      // Mock bcrypt.hash to simulate password hashing
      bcrypt.hash = jest.fn().mockResolvedValue(hashedPassword);

      // Mock UserClass.create to simulate creating the user
      (UserClass.prototype.create as jest.Mock).mockResolvedValue({ ...mockUser, ...newUser });

      // Mock JWT sign to simulate generating a token
      (jwt.sign as jest.Mock).mockReturnValue(token);

      const res = await request.post('/users').send(newUser);

      expect(res.status).toBe(201);
      expect(res.body.token).toBe(token);
      expect(UserClass.prototype.create).toHaveBeenCalledWith({
        firstname: 'Jane',
        lastname: 'Doe',
        password_digest: hashedPassword,
      });
    });

    it('should return 400 if user creation fails', async () => {
      const newUser = { firstname: 'Jane', lastname: 'Doe', password: 'password123' };

      // Mock UserClass.create to throw an error
      (UserClass.prototype.create as jest.Mock).mockRejectedValue(new Error('User creation failed'));

      const res = await request.post('/users').send(newUser);

      expect(res.status).toBe(400);
      expect(res.body.error).toBe('Failed to create user: User creation failed');
    });
  });
});
