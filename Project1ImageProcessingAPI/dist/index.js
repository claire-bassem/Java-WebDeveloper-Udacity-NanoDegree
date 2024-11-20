"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
var express_1 = __importDefault(require("express"));
var images_1 = __importDefault(require("./routes/images"));
//import sharp from 'sharp';
var app = (0, express_1.default)();
var port = 3000;
// const inputFile = './users.csv';
// const outputFile = 'users.json';
// Route handling
app.use('/api/images', images_1.default);
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
app.listen(port, function () {
    console.log("server started at localhost:".concat(port));
});
exports.default = app;
