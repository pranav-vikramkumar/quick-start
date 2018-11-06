let {printSchema} = require('graphql');
let schema = require("./schema")
console.log(printSchema(schema))

let express = require('express');
let express_graphql = require('express-graphql');
let server = express();

let cors = require('cors');
server.use(cors());

server.use('/graphql',express_graphql({
	schema:schema,
	graphiql:true
}));

server.listen(4000,()=>{
	console.log("GraphQL now running on port 4000");
})

/************
Test Queries:

1) Get all authors and their books
{
  authors {
    name
    books {
      name
    }
  }
}
2) Get all books and their authors
{
  books {
    name
    author {
      name
    }
  }
}
3) Add a new Author
mutation{
  addAuthor(name:"Praveen",age:31){
    name
  }
}
*/