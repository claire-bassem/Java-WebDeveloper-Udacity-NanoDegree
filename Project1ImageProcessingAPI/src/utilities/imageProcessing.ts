import sharp from 'sharp';
import path from 'path';
import fs from 'fs';

export const resizeImage = async (
  filename: string,
  width: number,
  height: number
): Promise<string> => {
  // const inputPath = path.join(
  //   __dirname,
  //   '..',
  //   'images',
  //   'full',
  //   `${filename}.jpg`
  // );
  // const outputPath = path.join(
  //   __dirname,
  //   '..',
  //   'images',
  //   'thumb',
  //   `${filename}_${width}x${height}.jpg`
  // );

  const imagesBasePath = path.join(__dirname, '..', '..', 'images'); // Adjusted path
  const inputPath = path.join(imagesBasePath, 'full', `${filename}.jpg`);
  const outputPath = path.join(
    imagesBasePath,
    'thumb',
    `${filename}_${width}x${height}.jpg`
  );

  if (!fs.existsSync(inputPath)) {
    throw new Error('File does not exist');
  }

  if (fs.existsSync(outputPath)) {
    return outputPath; // Return cached image if it exists
  }

  await sharp(inputPath)
    .resize(width, height)
    .toFormat('jpeg')
    .toFile(outputPath);

  return outputPath;
};
