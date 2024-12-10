// import { Request, Response } from 'express'
// import express from 'express';

// import bodyParser from 'body-parser'

// const app: express.Application = express()
// const address: string = "0.0.0.0:3000"

// app.use(bodyParser.json())

// app.get('/', function (req: Request, res: Response) {
//     res.send('Hello World!')
// })

// app.listen(3000, function () {
//     console.log(`starting app on: ${address}`)
// })

import express from 'express';
import bodyParser from 'body-parser';
import userRoutes from './handlers/users_handler';
import productsRoutes from './handlers/products_handler';
import orderRoutes from './handlers/orders_handler';
const app: express.Application = express();
const address: string = '3000';

app.use(bodyParser.json());

userRoutes(app);
productsRoutes(app);
orderRoutes(app);
app.listen(3000, function () {
  console.log(`starting app on: ${address}`);
});

export default app;