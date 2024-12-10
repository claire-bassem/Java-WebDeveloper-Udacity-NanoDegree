import Client from '../../database';
import { User } from '../users';
import { UserClass } from '../users';

const store = new UserClass(); // Initialize the UserClass
let userId: number; // Variable to store the user ID after creation

describe("User Model", () => {
  // Before all tests, create a user to use in the tests
  beforeAll(async () => {
    const createdUser = await store.create({
      firstname: 'John',
      lastname: 'Doe',
      password_digest: 'password123',
    });
    userId = createdUser.id as number; // Store the ID of the created user
  });

  // After all tests, delete the created user from the database
  afterAll(async () => {
    const conn = await Client.connect();
    const sql = 'DELETE FROM users WHERE id=($1)';
    await conn.query(sql, [userId]);
    conn.release();
  });

  it('should have an index method', () => {
    expect(store.index).toBeDefined();
  });

  it('should have a show method', () => {
    expect(store.show).toBeDefined();
  });

  it('should have a create method', () => {
    expect(store.create).toBeDefined();
  });

  it('should have an authenticate method', () => {
    expect(store.authenticate).toBeDefined();
  });

  it('index method should return a list of users', async () => {
    const result = await store.index();
    expect(result.length).toBeGreaterThan(0); // Check if there's at least one user
  });



  it('create method should add a new user', async () => {
    const newUser: User = {
      firstname: 'Jane',
      lastname: 'Smith',
      password_digest: 'password123',
    };
    const result = await store.create(newUser);
    expect(result.firstname).toBe(newUser.firstname);
    expect(result.lastname).toBe(newUser.lastname);
    expect(result.password_digest).toBeDefined(); // Ensure the password_digest is generated
  });

  it('authenticate method should return user with correct credentials', async () => {
    const authenticatedUser = await store.authenticate('JohnDoe', 'password123');
    expect(authenticatedUser).toBeTruthy();
    expect(authenticatedUser?.firstname).toBe('John');
    expect(authenticatedUser?.lastname).toBe('Doe');
  });

  it('authenticate method should return null for incorrect credentials', async () => {
    const authenticatedUser = await store.authenticate('JohnDoe', 'wrongpassword');
    expect(authenticatedUser).toBeNull();
  });
});
