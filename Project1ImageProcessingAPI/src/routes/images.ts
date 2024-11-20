import express, { Request, Response, Router } from 'express';
import { resizeImage } from '../utilities/imageProcessing';

const router: Router = express.Router();

router.get('/', async (req: Request, res: Response): Promise<void> => {
  try {
    const { filename, width, height } = req.query;

    // Check for missing parameters
    if (!filename || !width || !height) {
      res
        .status(400)
        .send(
          'Missing required query parameters. Please provide "filename", "width", and "height".'
        );
      console.error('Error: Missing filename, width, or height.');
      return;
    }

    // Regular expression for checking that width and heights are only numbers
    const numericRegex = /^[0-9]+$/;

    if (!numericRegex.test(width as string)) {
      res
        .status(400)
        .send(
          'Invalid "width" parameter. It must be a positive integer with no characters.'
        );
      console.error(`Error: Width contains invalid characters: "${width}".`);
      return;
    }

    if (!numericRegex.test(height as string)) {
      res
        .status(400)
        .send(
          'Invalid "height" parameter. It must be a positive integer with no characters.'
        );
      console.error(`Error: Height contains invalid characters: "${height}".`);
      return;
    }

    // Convert width and height to numbers
    const widthNum = parseInt(width as string, 10);
    const heightNum = parseInt(height as string, 10);

    if (widthNum <= 0) {
      res
        .status(400)
        .send(
          'Invalid "width" parameter. It must be a positive integer greater than zero.'
        );
      console.error(`Error: Invalid width "${width}".`);
      return;
    }

    if (heightNum <= 0) {
      res
        .status(400)
        .send(
          'Invalid "height" parameter. It must be a positive integer greater than zero.'
        );
      console.error(`Error: Invalid height "${height}".`);
      return;
    }

    // Validate filename (ensure it does not contain unsupported characters)
    const validFilenameRegex = /^[a-zA-Z0-9_-]+$/;
    if (!validFilenameRegex.test(filename as string)) {
      res
        .status(400)
        .send(
          'Invalid "filename" parameter. Only alphanumeric characters, hyphens, and underscores are allowed.'
        );
      console.error(`Error: Invalid filename "${filename}".`);
      return;
    }

    // Process image
    const outputPath = await resizeImage(
      filename as string,
      widthNum,
      heightNum
    );

    // Send file response
    res.sendFile(outputPath, (err) => {
      if (err) {
        console.error('Error sending file:', err);
        if (!res.headersSent) {
          res
            .status(500)
            .send('An unexpected error occurred while sending the file.');
        }
      } else {
        console.log(
          `Successfully processed file. Parameters: filename=${filename}, width=${widthNum}, height=${heightNum}`
        );
        console.log(`Output Path: ${outputPath}`);
      }
    });
  } catch (error) {
    console.error('Error processing request:', error);
    if (!res.headersSent) {
      res.status(500).send('An unexpected server error occurred.');
    }
  }
});

export default router;
