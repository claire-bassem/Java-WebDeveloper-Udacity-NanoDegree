# Image Processing API

An image processing API built with **Node.js**, **TypeScript**, and **Sharp**. This API resizes images dynamically and caches the resized versions for optimized performance.

---

## Table of Contents
- [Features](#features)
- [Scripts](#scripts)
- [Endpoints](#endpoints)

---

## Features
- Resize images dynamically by specifying dimensions (`width` and `height`).
- Cache resized images to avoid redundant processing.
- Validates inputs and provides meaningful error messages.
- Organized codebase with separation of concerns (routes, utilities, and server logic).
- Includes comprehensive unit and integration tests.

---

## Scripts
These scripts are used to build, test, and run the application:

1. **Start the application**:
  
   npm run start

2. **Run Tests**:

    npm run test

3. **Build the application**:
   
   npm run build

4. **Lint the code**:

    npm run lint

5. **Prettify the code**:

    npm run prettier


## Endpoints

### `GET /api/images`

- **Description**: Processes an image from the `images/full` directory and returns a resized version based on the specified dimensions.

- **Parameters**:
  - `filename` (string, required): Name of the image file (without extension) located in the `images/full` directory.
  - `width` (integer, required): Desired width for resizing the image.
  - `height` (integer, required): Desired height for resizing the image.

- **Example**:
  GET http://localhost:3000/api/images?filename=santamonica&width=200&height=200
