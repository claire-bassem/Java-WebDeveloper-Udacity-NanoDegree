import express from 'express';
import logger from './utilities/logger';
import csv from 'csvtojson';
import { promises as fspromises } from 'fs';
import imagesRoute from './routes/images';

//import sharp from 'sharp';

const app = express();

const port = 3000;

// const inputFile = './users.csv';
// const outputFile = 'users.json';

// Route handling
app.use('/api/images', imagesRoute);

// app.get('/convert', async (req, res) => {
//   try {
//     const data = await csv().fromFile(inputFile);

//     const newData = data.map(
//       (item: { first_name: string; last_name: string; phone: string }) => {
//         let phone = item.phone;
//         if (!phone) {
//           phone = 'missing data';
//         }
//         return {
//           first: item.first_name,
//           last: item.last_name,
//           phone,
//         };
//       }
//     );

//     await fspromises.writeFile(outputFile, JSON.stringify(newData, null, 2));

//     res.send('File converted successfully!');
//   } catch (error) {
//     console.error('Error during conversion:', error);
//     res.status(500).send('An error occurred during the conversion process.');
//   }
// });

// app.get('/api', (req, res) => {
//   res.send('Hello, world   !');
// });

// app.get('/route', logger, (req, res) => {
//     res.send('success!');
//   });

app.listen(port, () => {
  console.log(`server started at localhost:${port}`);
});
export default app;
