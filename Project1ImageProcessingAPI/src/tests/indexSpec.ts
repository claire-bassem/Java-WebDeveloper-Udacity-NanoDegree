import supertest from 'supertest';
import app from '../index';
import { resizeImage } from '../utilities/imageProcessing';

const request = supertest(app);

describe('Image Processing API', () => {
  it('should return a 200 status for the /api/images endpoint', async () => {
    const response = await request.get(
      '/api/images?filename=santamonica&width=200&height=200'
    );
    expect(response.status).toBe(200);
  });

  it('should not throw an error during image transformation', async () => {
    await expectAsync(resizeImage('santamonica', 200, 200)).not.toBeRejected();
  });

  it('should throw a specific error for missing file', async () => {
    await expectAsync(
      resizeImage('nonexistent', 200, 200)
    ).toBeRejectedWithError('File does not exist');
  });
});
