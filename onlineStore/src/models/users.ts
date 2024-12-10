import Client from '../database';
import bcrypt from 'bcrypt';
export type User = {
     id?: number;
     firstname: string;
  lastname: string;
     password_digest: string;

}
const pepper = process.env.PEPPER;
const saltRounds = process.env.SALT_ROUNDS || "10";
export class UserClass {

    
    async index(): Promise<User[]> {
      try {
        const conn = await Client.connect();
        const sql = 'SELECT * FROM users';
        const result = await conn.query(sql);
        conn.release();
        return result.rows;
      } catch (err) {
        throw new Error(`Could not get users. Error: ${err}`);
      }
    }
  
    async show(id: string): Promise<User | null> {
      try {
        const conn = await Client.connect();
        const sql = 'SELECT *  FROM users WHERE id=($1)';
        const result = await conn.query(sql, [id]);
        conn.release();
        return result.rows[0] || null;
      } catch (err) {
        throw new Error(`Could not find user ${id}. Error: ${err}`);
      }
    }
  
    async create(u: User): Promise<User> {
        try {
          console.log("Pepper:", pepper);
console.log("Salt Rounds:", saltRounds);

          console.log("1");
          const conn = await Client.connect()
          console.log("2");
          const test = await conn.query('SELECT NOW()');
          console.log("Test query result:", test.rows[0]);
          const sql = 'INSERT INTO users (firstname, lastname, password_digest) VALUES($1, $2, $3) RETURNING *'
          console.log("3");

          const hash = bcrypt.hashSync(
            u.password_digest + pepper, 
            parseInt(saltRounds)
          );
          console.log("4");
          console.log("Executing query:", sql);
          console.log("Query parameters:", [u.firstname, u.lastname, hash]);
          const result = await conn.query(sql, [u.firstname, u.lastname, hash])
          console.log("Query result:", result);

          console.log("5");

          const user = result.rows[0]
          console.log("6");

          conn.release()
          console.log("7");
console.log(user);
          return user
          console.log("8");

        } catch(err) {
          throw new Error(`unable create user (${u.firstname}): ${err}`)
        } 
      }
  

    async authenticate(username: string, password: string): Promise<User | null> {
        const conn = await Client.connect()
        const sql = 'SELECT password_digest FROM users WHERE username=($1)'
    
        const result = await conn.query(sql, [username])
    
        console.log(password+pepper)
    
        if(result.rows.length) {
    
          const user = result.rows[0]
    
          console.log(user)
    
          if (bcrypt.compareSync(password+pepper, user.password_digest)) {
            return user
          }
        }
    
        return null
      }
  }
  