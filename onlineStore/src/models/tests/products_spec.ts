import { ProductClass } from '../products';
import { Product } from '../products';
const store = new ProductClass(); // Initialize the ProductClass
let productId: number; // Variable to store the product ID after creation

describe("Product Model", () => {
  // Before all tests, create a product to use in the tests
  beforeAll(async () => {
    const createdProduct = await store.create({
      name: 'Test Product',
      price: 19.99,
    });
    productId = createdProduct.id as number; // Store the ID of the created product
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

  it('index method should return a list of products', async () => {
    const result = await store.index();
    expect(result.length).toBeGreaterThan(0); // Check if there's at least one product
  });

  it('show method should return the correct product', async () => {
    const result = await store.show(productId.toString());
    expect(result).toEqual({
      id: productId,
      name: 'Test Product',
      price: 19.99,
    });
  });

  it('create method should add a product', async () => {
    const newProduct: Product = {
      name: 'New Product',
      price: 29.99,
    };
    const result = await store.create(newProduct);
    expect(result.name).toBe(newProduct.name);
    expect(result.price).toBe(newProduct.price);
  });

  
});
